package glmodel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 *  Loads a library of materials from a .mtl file into an array of GLMaterial objects.
 */
public class GLMaterialLib {
    public static final int SIZE_INT = 4;
    public static final int SIZE_BYTE = 1;
    public static final int SIZE_FLOAT = 4;
    
    static Hashtable<String, String> OpenGLextensions;       // will be populated by extensionExists()
    
    // path to the .mtl file (loadMaterial() will set these)
    // we'll load texture images from same folder as material file
    public String filepath = "";
    public String filename = "";

    // array of materials loaded from .mtl file
    GLMaterial[] materials;


    public GLMaterialLib(String mtlFilename) {
    	if (mtlFilename != null && mtlFilename.length() > 0) {
    		materials = loadMaterials(mtlFilename);
    	}
    }

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *
	 * functions to load and save materials (from/to .mtl file)
	 *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public GLMaterial[] loadMaterials(String mtlFilename) {
        GLMaterial[] mtls = null;
        // Separate leading path from filename (we'll load textures from same folder)
        String[] pathParts = GL_OBJ_Reader.getPathAndFile(mtlFilename);
        filepath = pathParts[0];
        filename = pathParts[1];
        // read the mtl file
        try {
        	mtls = loadMaterials(new BufferedReader(new InputStreamReader(GL_OBJ_Reader.getInputStream(mtlFilename))));
        }
        catch (Exception e) {
        	System.out.println("GLMaterialLib.loadMaterials(): Exception when loading " + mtlFilename + ": " + e);
        }
    	return mtls;
    }

    /**
     * MTL file format in a nutshell:
     *
     * <PRE>
     * 		newmtl white          // begin material and specify name
     * 		Kd 1.0 1.0 1.0        // diffuse rgb
     * 		Ka 0.2 0.2 0.2        // ambient rgb
     * 		Ks 0.6 0.6 0.6        // specular rgb
     * 		Ns 300                // shininess 0-1000
     *      d 0.5                 // alpha 0-1
     *      map_Kd texture.jpg    // texture file
     *                            // blank line ends material definition
     * </PRE>
     */
    public GLMaterial[] loadMaterials(BufferedReader br) {
    	ArrayList<GLMaterial> mtlslist = new ArrayList<GLMaterial>();
    	GLMaterial material = null;
    	String line = "";

    	float[] rgb;
    	try {
    		while ((line = br.readLine()) != null) {
    			// remove extra whitespace
    			line = line.trim();
    			if (line.length() > 0) {
    				if (line.startsWith("#")) {
    					// ignore comments
    				}
    				else if (line.startsWith("newmtl")) {
    					// newmtl some_name
    					material = new GLMaterial();  // start new material
    					material.setName(line.substring(7));
    					mtlslist.add(material);      // add to list
    				}
    				else if (line.startsWith("Kd")) {
    					// Kd 1.0 0.0 0.5
    					if ((rgb = read3Floats(line)) != null) {
    						material.setDiffuse(rgb);
    					}
    				}
    				else if (line.startsWith("Ka")) {
    					// Ka 1.0 0.0 0.5
    					if ((rgb = read3Floats(line)) != null) {
    						material.setAmbient(rgb);
    					}
    				}
    				else if (line.startsWith("Ks")) {
    					// Ks 1.0 0.0 0.5
    					if ((rgb = read3Floats(line)) != null) {
    						material.setSpecular(rgb);
    					}
    				}
    				else if (line.startsWith("Ns")) {
    					// Ns 500.5
    					// shininess in mtl file is 0-1000
    					if ((rgb = read3Floats(line)) != null) {
    						// convert to opengl 0-127
    						int shininessValue = (int) ((rgb[0] / 1000f) * 127f);
    						material.setShininess( shininessValue );
    					}
    				}
    				else if (line.startsWith("d")) {
    					// d 1.0
    					// alpha value of material 0=transparent 1=opaque
    					if ((rgb = read3Floats(line)) != null) {
    						material.setAlpha( rgb[0] );
    					}
    				}
    				else if (line.startsWith("illum")) {
    					// illum (0, 1, or 2)
    					// lighting for material 0=disable, 1=ambient & diffuse (specular is black), 2 for full lighting.
    					if ((rgb = read3Floats(line)) != null) {
    						// not yet
    					}
    				}
    				else if (line.startsWith("map_Kd")) {
    					// map_Kd filename
    					// add a texture to the material
    					String textureFile = line.substring(7);
    			        if (textureFile != null && !textureFile.equals("")) {
        					int textureHandle = 0;
    			        	try {
    					    	textureHandle = makeTexture(filepath + textureFile);
    			        	}
    			        	catch (Exception e) {
    			        		System.out.println("GLMaterialLib.loadMaterials(): could not load texture file (" +line+ ")" + e);
    			        	}
        					material.setTextureFile(textureFile);
        					material.setTexture(textureHandle);
    			        }
    				}
    			}
    		}
    	} catch (Exception e) {
    		System.out.println("GLMaterialLib.loadMaterials() failed at line: " + line);
    	}
    	// debug:
    	System.out.println("GLMaterialLib.loadMaterials(): loaded " + mtlslist.size() + " materials ");
    	// return array of materials
    	GLMaterial[] mtls = new GLMaterial[ mtlslist.size() ];
    	mtlslist.toArray(mtls);
    	return mtls;
    }

    // always return array of four floats (usually containing RGBA, but
    // in some cases contains only one value at pos 0).
    private float[] read3Floats(String line)
    {
    	try
    	{
    		StringTokenizer st = new StringTokenizer(line, " ");
    		st.nextToken();   // throw out line identifier (Ka, Kd, etc.)
    		if (st.countTokens() == 1) {
    			return new float[] {Float.parseFloat(st.nextToken()), 0f, 0f, 0f};
    		}
    		else if (st.countTokens() == 3) { // RGBA (force A to 1)
    			return new float[] {Float.parseFloat(st.nextToken()),
    					Float.parseFloat(st.nextToken()),
    					Float.parseFloat(st.nextToken()),
    					1f };
    		}
    	}
    	catch (Exception e)
    	{
    		System.out.println("GLMaterialLib.read3Floats(): error on line '" + line + "', " + e);
    	}
    	return null;
    }

    /**
     * Write an array of GLMaterial objects to a .mtl file.
     * @param mtls      array of materials to write to file
     * @param filename  name of .mtl file
     */
    public void writeLibe(GLMaterial[] mtls, String filename) {
    	try {
    		PrintWriter mtlfile = new PrintWriter(new FileWriter(filename));
    		writeLibe(mtls, mtlfile);
    		mtlfile.close();
    	} catch (IOException e) {
    		System.out.println("GLMaterialLib.writeLibe(): IOException:" + e);
    	}
    }

    public void writeLibe(GLMaterial[] mtls, PrintWriter out) {
    	if (out != null) {
    		out.println("#");
    		out.println("# Wavefront material file for use with OBJ file");
    		out.println("# Created by GLMaterialLib.java");
    		out.println("#");
    		out.println("");
    		for (int i = 0; i < mtls.length; i++) {
    			write(out, mtls[i]);
    		}
    	}
    }

    /**
     *  Write one material.
     */
    public void write(PrintWriter out, GLMaterial mtl)
    {
    	if (out != null) {
    		out.println("newmtl " + mtl.mtlname);
    		out.println("Ka " + mtl.ambient.get(0) + " " + mtl.ambient.get(1) + " " + mtl.ambient.get(2));
    		out.println("Kd " + mtl.diffuse.get(0) + " " + mtl.diffuse.get(1) + " " + mtl.diffuse.get(2));
    		out.println("Ks " + mtl.specular.get(0) + " " + mtl.specular.get(1) + " " + mtl.specular.get(2));
    		out.println("Ns " + ( (mtl.shininess.get(0) / 128.0) * 1000.0));
    		if (mtl.textureFile != null && !mtl.textureFile.equals("")) {
    			out.println("map_Kd " + mtl.textureFile);
    		}
    		if (mtl.getAlpha() != 1f) {
    			out.println("d " + mtl.getAlpha());
    		}
    		out.println("");
    	}
    }

    /**
     * return a duplicate of this material.  all values are duplicated except
     * the texture, which is passed by reference to the clone (to prevent
     * multiple copies of the same texture).
     *
     * @return the cloned material
     */
    public GLMaterial getClone(GLMaterial mtl) {
    	GLMaterial clone = new GLMaterial();
    	clone.setDiffuse( new float[] {mtl.diffuse.get(0),mtl.diffuse.get(1),mtl.diffuse.get(2),mtl.diffuse.get(3)} );
    	clone.setAmbient( new float[] {mtl.ambient.get(0),mtl.ambient.get(1),mtl.ambient.get(2),mtl.ambient.get(3)} );
    	clone.setSpecular( new float[] {mtl.specular.get(0),mtl.specular.get(1),mtl.specular.get(2),mtl.specular.get(3)} );
    	clone.setGlowColor( new float[] {mtl.emission.get(0),mtl.emission.get(1),mtl.emission.get(2),mtl.emission.get(3)} );
    	clone.setShininess( mtl.shininess.get(0) );
    	// set the texture filename and handle in the clone (clones share 1 texture)
    	clone.textureFile = mtl.textureFile;
    	clone.textureHandle = mtl.textureHandle;
    	clone.setName( mtl.mtlname + "-copy");
    	return clone;
    }

    /**
     * find a material by name in an array of GLMaterial objects
     */
    public GLMaterial find(String materialName) {
    	int mtl_idx = findID(materialName);
    	if (mtl_idx >= 0) {
    		return materials[mtl_idx];
    	}
    	return null;
    }

    /**
     * find a material by name in an array of GLMaterial objects
     * return the array index of the material
     */
    public int findID(String materialName) {
    	if (materials != null && materialName != null) {
    		for (int m=0; m < materials.length; m++) {
    			if (materials[m].mtlname.equals(materialName)) {
    				return m;
    			}
    		}
    	}
    	return -1;
    }

    /**
     * Load an image from the given file and return a GLImage object.
     * @param image filename
     * @return the loaded GLImage
     */
    public static GLImage loadImage(String imgFilename) {
        GLImage img = new GLImage(imgFilename);
        if (img.isLoaded()) {
            return img;
        }
        return null;
    }

    /**
     * Build Mipmaps for currently bound texture (builds a set of textures at various
     * levels of detail so that texture will scale up and down gracefully)
     *
     * @param textureImg  the texture image
     * @return   error code of buildMipMap call
     */
    public static int makeTextureMipMap(int textureHandle, GLImage textureImg)
    {
      int ret = 0;
      if (textureImg != null && textureImg.isLoaded()) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        ret = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8,
            textureImg.w, textureImg.h,
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureImg.getPixelBytes());
        if (ret != 0) {
          System.out.println("GLApp.makeTextureMipMap(): Error occured while building mip map, ret=" + ret + " error=" + GLU.gluErrorString(ret) );
        }
        // Assign the mip map levels and texture info
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
      }
        return ret;
    }

    /**
     * Create a texture and mipmap from the given image file.
     */
    public static int makeTexture(String textureImagePath)
    {
    int textureHandle = 0;
    GLImage textureImg = loadImage(textureImagePath);
    if (textureImg != null) {
      textureHandle = makeTexture(textureImg);
      makeTextureMipMap(textureHandle,textureImg);
    }
    return textureHandle;
    }

    /**
     * Create a texture and optional mipmap with the given parameters.
     *
     * @param mipmap: if true, create mipmaps for the texture
     * @param anisotropic: if true, enable anisotropic filtering
     */
    public static int makeTexture(String textureImagePath, boolean mipmap, boolean anisotropic)
    {
        int textureHandle = 0;
        GLImage textureImg = loadImage(textureImagePath);
        if (textureImg != null) {
            textureHandle = makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, anisotropic);
            if (mipmap) {
                makeTextureMipMap(textureHandle,textureImg);
            }
        }
        return textureHandle;
    }

    /**
     * Create a texture from the given image.
     */
    public static int makeTexture(GLImage textureImg)
    {
        if ( textureImg != null ) {
            if (isPowerOf2(textureImg.w) && isPowerOf2(textureImg.h)) {
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            }
            else {
                System.out.println("GLApp.makeTexture(GLImage) Warning: not a power of two: " + textureImg.w + "," + textureImg.h);
                textureImg.convertToPowerOf2();
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            }
        }
        return 0;
    }

    /**
     * Create a blank square texture with the given size.
     * @return  the texture handle
     */
    public static int makeTexture(int w)
    {
        ByteBuffer pixels = allocBytes(w*w*SIZE_INT);  // allocate 4 bytes per pixel
        return makeTexture(pixels, w, w, false);
  }

    /**
     * Create a texture from the given pixels in the default Java ARGB int format.<BR>
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(int[] pixelsARGB, int w, int h, boolean anisotropic)
    {
      if (pixelsARGB != null) {
        ByteBuffer pixelsRGBA = GLImage.convertImagePixelsRGBA(pixelsARGB,w,h,true);
        return makeTexture(pixelsRGBA, w, h, anisotropic);
      }
      return 0;
    }

    /**
     * Create a texture from the given pixels in the default OpenGL RGBA pixel format.
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(ByteBuffer pixels, int w, int h, boolean anisotropic)
    {
        // get a new empty texture
        int textureHandle = allocateTexture();
        // preserve currently bound texture, so glBindTexture() below won't affect anything)
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);

        // make texture "anisotropic" so it will minify more gracefully
      if (anisotropic && extensionExists("GL_EXT_texture_filter_anisotropic")) {
        // Due to LWJGL buffer check, you can't use smaller sized buffers (min_size = 16 for glGetFloat()).
        FloatBuffer max_a = allocFloats(16);
        // Grab the maximum anisotropic filter.
        GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max_a);
        // Set up the anisotropic filter.
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_a.get(0));
      }

        // Create the texture from pixels
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
            0,            // level of detail
            GL11.GL_RGBA8,      // internal format for texture is RGB with Alpha
            w, h,           // size of texture image
            0,            // no border
            GL11.GL_RGBA,       // incoming pixel format: 4 bytes in RGBA order
            GL11.GL_UNSIGNED_BYTE,  // incoming pixel data type: unsigned bytes
            pixels);        // incoming pixels

        // restore previous texture settings
        GL11.glPopAttrib();

        return textureHandle;
    }

    /**
     *  Returns true if n is a power of 2.  If n is 0 return zero.
     */
    public static boolean isPowerOf2(int n) {
      if (n == 0) { return false; }
        return (n & (n - 1)) == 0;
    }
    public static ByteBuffer allocBytes(int howmany) {
      return ByteBuffer.allocateDirect(howmany * SIZE_BYTE).order(ByteOrder.nativeOrder());
    }

    public static FloatBuffer allocFloats(int howmany) {
      return ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static IntBuffer allocInts(int howmany) {
      return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    /**
     * Return true if the OpenGL context supports the given OpenGL extension.
     */
    public static boolean extensionExists(String extensionName) {
      if (OpenGLextensions == null) {
        String[] GLExtensions = GL11.glGetString(GL11.GL_EXTENSIONS).split(" ");
        OpenGLextensions = new Hashtable<String, String>();
        for (int i=0; i < GLExtensions.length; i++) {
          OpenGLextensions.put(GLExtensions[i].toUpperCase(),"");
        }
      }
      return (OpenGLextensions.get(extensionName.toUpperCase()) != null);
    }
    /**
     * Allocate a texture (glGenTextures) and return the handle to it.
     */
    public static int allocateTexture()
    {
        IntBuffer textureHandle = allocInts(1);
        GL11.glGenTextures(textureHandle);
        return textureHandle.get(0);
    }

}