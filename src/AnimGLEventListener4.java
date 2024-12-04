import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener4 extends AnimListener {
    double a=0;
    int animationIndex = 0;
    int monsterIndex = 7;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth / 2, y = 0;
    int x1 = maxWidth / 3, y1 = maxHeight - 10;
    boolean StarThrown = false;
    int starX, starY;
    double starSpeed = 0.5;
    int starIndex = 4;
    int health = 10;
    String []textureNames = {"Man1.png", "Man2.png", "Man3.png", "Man4.png", "ninja star.png", "Health.png", "HealthB.png", "a.png", "b.png", "c.png",
            "d.png", "e.png", "f.png", "g.png", "h.png", "i.png", "j.png", "k.png", "l.png", "m.png",
            "n.png", "o.png", "p.png", "q.png", "r.png", "s.png", "t.png", "u.png", "v.png", "w.png",
            "x.png", "y.png", "z.png", "Back.png"};

    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    int []textures = new int[textureNames.length];
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(GL.GL_TEXTURE_2D, GL.GL_RGBA, texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture[i].getPixels());
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void display(GLAutoDrawable gld) {
        y1--;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        DrawBackground(gl);
        handleKeyPress();
        animationIndex = animationIndex % 4;
        if (StarThrown) {
            starY += starSpeed;
            starX += (x1 - starX) / 8;
            starY += (y1 - starY) / 2;
            if (starY > maxHeight) {
                starY = 0;
                starX = (int) (Math.random() * maxWidth);
            }
            if (y1 < 0) {
                StarThrown = false;
            }
            gl.glPushMatrix();
            a+=90;
            gl.glRotated(0, 0, 1, a);
            DrawSprite(gl, starX, starY, starIndex, 0.5F);
            if (sqrdDistance(starX, starY, x1, y1) < 100) {
                x1 = (int) (Math.random() * maxWidth);
                y1 = maxHeight;
                monsterIndex = (int) (Math.random() * 26) + 7;
                StarThrown = false;
            }
        }
        DrawSprite(gl, x, y, animationIndex, 1);
        DrawSprite(gl, x1, y1, monsterIndex, 1);

        if (y1 < 0) {
            x1 = (int) (Math.random() * maxWidth);
            y1 = maxHeight;
            monsterIndex = (int) (Math.random() * 26) + 7;
            health--;
            System.out.println("Health: " + health);
            if (health <= 0) {
                System.out.println("Game Over");
                System.exit(0);
            }
        }
        DrawHealth(gl);

    }
    public void DrawHealth(GL gl) {
        for (int i = 0; i < 5; i++) {
            gl.glEnable(GL.GL_BLEND);

            if (i < health) {
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            } else {
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);
                gl.glColor4f(1.0f, 1.0f, 1.0f, 1f);
            }

            gl.glPushMatrix();
            gl.glTranslated(-0.9 + (i * 0.1), 0.9, 0);
            gl.glScaled(0.05, 0.05, 1);

            gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, -1.0f);
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f(1.0f, 1.0f, -1.0f);
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
            gl.glPopMatrix();
            gl.glDisable(GL.GL_BLEND);
        }

        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }


    public double sqrdDistance(int x, int y, int x1, int y1) {
        return Math.pow(x - x1, 2) + Math.pow(y - y1, 2);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }


    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }


    public void handleKeyPress() {
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 0) x--;
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth - 10) x++;
            animationIndex++;
        }


        char c = Character.toUpperCase(textureNames[monsterIndex].charAt(0));
        if (isKeyPressed(KeyEvent.getExtendedKeyCodeForChar(c))) {
            if (!StarThrown) {
                StarThrown = true;
                starX = x;
                starY = y;
                System.out.println("Star with key: " + c);
            }
        }
    }
    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
}
}
