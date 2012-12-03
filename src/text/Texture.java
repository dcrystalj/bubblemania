package text;
/* 
 * Copyright (c) 2002-2004 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture {


  public static IntBuffer loadTextures2D(String[] p_strNames)
  {
    if (p_strNames==null || p_strNames.length==0)
      return null;
    
    // generate one texture ID
    IntBuffer ibID = BufferUtils.createIntBuffer(p_strNames.length);
    GL11.glGenTextures(ibID);            

    for (int nI=0;nI<p_strNames.length;nI++)
    {
      // select texture with this ID
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, ibID.get(nI));
  
      // when texture area is small, bilinear filter the closest mipmap
      //GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_NEAREST );
      GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); 
      // when texture area is large, bilinear filter the original
      GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );

      // the texture wraps over at the edges (repeat)
      GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT );
      GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT );
            
      // load image from disk
      BufferedImage bufferedImage = null;
      try {
        bufferedImage = ImageIO.read(new FileInputStream(p_strNames[nI]));
      }
      catch (Exception exp)
      {
        System.err.println(exp.getMessage());
        continue;
      }
      
      // convert that image into a byte buffer of texture data 
      ByteBuffer textureBuffer = Texture.convertImageData(bufferedImage);     
      
      // produce a texture from the byte buffer
      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 
                    0, 
                    GL11.GL_RGBA, 
                    get2Fold(bufferedImage.getWidth()), 
                    get2Fold(bufferedImage.getHeight()), 
                    0, 
                    GL11.GL_RGBA, 
                    GL11.GL_UNSIGNED_BYTE, 
                    textureBuffer );
    }
    
    return ibID;
  }
  
  /**
   * Convert the buffered image to a texture
   *
   * @param bufferedImage The image to convert to a texture
   * @param texture The texture to store the data into
   * @return A buffer containing the data
   */
  private static ByteBuffer convertImageData(BufferedImage bufferedImage) { 
      ByteBuffer imageBuffer = null; 
      WritableRaster raster;
      BufferedImage texImage;
      
      int texWidth = 2;
      int texHeight = 2;
      
      // find the closest power of 2 for the width and height
      // of the produced texture
      while (texWidth < bufferedImage.getWidth()) {
          texWidth *= 2;
      }
      while (texHeight < bufferedImage.getHeight()) {
          texHeight *= 2;
      }
            
      ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
          new int[] {8,8,8,8},
          true,
          false,
          ComponentColorModel.TRANSLUCENT,
          DataBuffer.TYPE_BYTE);
      
      // create a raster that can be used by OpenGL as a source for a texture
      raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
      texImage = new BufferedImage(glColorModel,raster,false,new Hashtable<Object, Object>());
          
      // copy the source image into the produced image
      Graphics g = texImage.getGraphics();
      g.setColor(new Color(0f,0f,0f,0f));
      g.fillRect(0,0,texWidth,texHeight);
      g.drawImage(bufferedImage,0,0,null);
      
      // build a byte buffer from the temporary image 
      // that be used by OpenGL to produce a texture.
      byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

      imageBuffer = ByteBuffer.allocateDirect(data.length); 
      imageBuffer.order(ByteOrder.nativeOrder()); 
      imageBuffer.put(data, 0, data.length); 
      imageBuffer.flip();
      
      return imageBuffer; 
  } 

  /**
   * Get the closest greater power of 2 to the fold number
   * 
   * @param fold The target number
   * @return The power of 2
   */
  private static int get2Fold(int fold) {
      int ret = 2;
      while (ret < fold) {
          ret *= 2;
      }
      return ret;
  } 
}