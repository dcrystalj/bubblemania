package text;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;


public class BitmapText
{
	 public int[] charPos = new int[2];
	 public int[] charCode = new int[2];
  private IntBuffer font;
  float fw = 0.065f, dx = 0.010f, fh = 0.13f, ff = 0.65f;
 
  public BitmapText() {
    font = Texture.loadTextures2D(new String[] { "font.png" });
    charPos[0]=10;
    charPos[1]=650;
  }
  
  public float textWidth(String s, int size) {
    return s.length()*size*ff;
  }
  
  public void renderString(String text, int size) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.get(0));
    int charposic=charPos[0];
    int len = text.length();

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    
    GL11.glBegin(GL11.GL_QUADS);
    for (int i = 0; i < len; i++) {
      charCode = getCode(text.charAt(i));
//      GL11.glColor4f(1, 0.0f + (float)i/(float)len, 0, 1f);
      GL11.glTexCoord2f(dx+charCode[1]*fw, (charCode[0]+1)*fh);      GL11.glVertex3f(charPos[0], charPos[1], 1);
      GL11.glTexCoord2f(dx+(charCode[1]+1)*fw, (charCode[0]+1)*fh);  GL11.glVertex3f(charPos[0]+ff*size, charPos[1], 1);
      GL11.glTexCoord2f(dx+(charCode[1]+1)*fw, charCode[0]*fh);      GL11.glVertex3f(charPos[0]+ff*size, charPos[1]+size, 1);
      GL11.glTexCoord2f(dx+charCode[1]*fw, charCode[0]*fh);          GL11.glVertex3f(charPos[0], charPos[1]+size, 1);
      charPos[0]+=ff*size;
    }
    charPos[0]= charposic;
    GL11.glEnd();

    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }
  
  private static int[] getCode(char c) {
    switch(c) {
    case 'a':
      return new int[] {0, 0};
    case 'b':
      return new int[] {0, 1};
    case 'c':
      return new int[] {0, 2};
    case 'è':
      return new int[] {0, 3};
    case 'd':
      return new int[] {0, 4};
    case 'e':
      return new int[] {0, 5};
    case 'f':
      return new int[] {0, 6};
    case 'g':
      return new int[] {0, 7};
    case 'h':
      return new int[] {0, 8};
    case 'i':
      return new int[] {0, 9};
    case 'j':
      return new int[] {0, 10};
    case 'k':
      return new int[] {0, 11};
    case 'l':
      return new int[] {0, 12};
    case 'm':
      return new int[] {0, 13};
    case 'n':
      return new int[] {0, 14};
    case 'o':
      return new int[] {1, 0};
    case 'p':
      return new int[] {1, 1};
    case 'q':
      return new int[] {1, 2};
    case 'r':
      return new int[] {1, 3};
    case 's':
      return new int[] {1, 4};
    case 'š':
      return new int[] {1, 5};
    case 't':
      return new int[] {1, 6};
    case 'u':
      return new int[] {1, 7};
    case 'v':
      return new int[] {1, 8};
    case 'w':
      return new int[] {1, 9};
    case 'x':
      return new int[] {1, 10};
    case 'y':
      return new int[] {1, 11};
    case 'z':
      return new int[] {1, 12};
    case 'ž':
      return new int[] {1, 13};
    case 'A':
      return new int[] {2, 0};
    case 'B':
      return new int[] {2, 1};
    case 'C':
      return new int[] {2, 2};
    case 'È':
      return new int[] {2, 3};
    case 'D':
      return new int[] {2, 4};
    case 'E':
      return new int[] {2, 5};
    case 'F':
      return new int[] {2, 6};
    case 'G':
      return new int[] {2, 7};
    case 'H':
      return new int[] {2, 8};
    case 'I':
      return new int[] {2, 9};
    case 'J':
      return new int[] {2, 10};
    case 'K':
      return new int[] {2, 11};
    case 'L':
      return new int[] {2, 12};
    case 'M':
      return new int[] {2, 13};
    case 'N':
      return new int[] {2, 14};
    case 'O':
      return new int[] {3, 0};
    case 'P':
      return new int[] {3, 1};
    case 'Q':
      return new int[] {3, 2};
    case 'R':
      return new int[] {3, 3};
    case 'S':
      return new int[] {3, 4};
    case 'Š':
      return new int[] {3, 5};
    case 'T':
      return new int[] {3, 6};
    case 'U':
      return new int[] {3, 7};
    case 'V':
      return new int[] {3, 8};
    case 'W':
      return new int[] {3, 9};
    case 'X':
      return new int[] {3, 10};
    case 'Y':
      return new int[] {3, 11};
    case 'Z':
      return new int[] {3, 12};
    case 'Ž':
      return new int[] {3, 13};
    case '0':
      return new int[] {4, 0};
    case '1':
      return new int[] {4, 1};
    case '2':
      return new int[] {4, 2};
    case '3':
      return new int[] {4, 3};
    case '4':
      return new int[] {4, 4};
    case '5':
      return new int[] {4, 5};
    case '6':
      return new int[] {4, 6};
    case '7':
      return new int[] {4, 7};
    case '8':
      return new int[] {4, 8};
    case '9':
      return new int[] {4, 9};
    case '!':
      return new int[] {5, 0};
    case '#':
      return new int[] {5, 1};
    case '$':
      return new int[] {5, 2};
    case '%':
      return new int[] {5, 3};
    case '&':
      return new int[] {5, 4};
    case '"':
      return new int[] {5, 5};
    case '/':
      return new int[] {5, 6};
    case '(':
      return new int[] {5, 7};
    case ')':
      return new int[] {5, 8};
    case '=':
      return new int[] {5, 9};
    case '?':
      return new int[] {5, 10};
    case '\'':
      return new int[] {5, 11};
    case '+':
      return new int[] {5, 12};
    case '*':
      return new int[] {5, 13};
    case '-':
      return new int[] {5, 14};
    case '_':
      return new int[] {6, 0};
    case '<':
      return new int[] {6, 1};
    case '>':
      return new int[] {6, 2};
    case '[':
      return new int[] {6, 3};
    case ']':
      return new int[] {6, 4};
    case '{':
      return new int[] {6, 5};
    case '}':
      return new int[] {6, 6};
    case ',':
      return new int[] {6, 7};
    case ';':
      return new int[] {6, 8};
    case '.':
      return new int[] {6, 9};
    case ':':
      return new int[] {6, 10};
    case '\\':
      return new int[] {6, 11};
    default:
      return new int[] {1, 14};
    }
  }
}
