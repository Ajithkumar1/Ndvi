package com.adero.thing;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import com.thingworx.logging.LogUtilities;
import com.thingworx.metadata.annotations.ThingworxBaseTemplateDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.things.Thing;
import org.slf4j.Logger;

@ThingworxBaseTemplateDefinition(name = "GenericThing")
public class Ndvi extends Thing {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger _logger = LogUtilities.getInstance().getApplicationLogger(Ndvi.class);

	public Ndvi() {
		// TODO Auto-generated constructor stub
	}

	@ThingworxServiceDefinition(name = "service1", description = "", category = "", isAllowOverride = false, aspects = {
			"isAsync:false" })
	@ThingworxServiceResult(name = "Result", description = "", baseType = "IMAGE", aspects = {})
	public byte[] service1(@ThingworxServiceParameter(name = "img", description = "", baseType = "IMAGE") byte[] img) {
		_logger.trace("Entering Service: service1");
		
		BufferedImage map = null;
		InputStream in = new ByteArrayInputStream(img);
        try {
			map = ImageIO.read(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        byte[] result;
		
	
		
        for (int y = 0; y < map.getHeight(); y++)
		{
			for (int x = 0; x < map.getWidth(); x++)
			{
		
		    Color c = new Color(map.getRGB(x, y));
		    float[] basec = c.getRGBColorComponents(null);
		    //int red = c.getRed();
		    //int green = c.getGreen();
		    //int blue = c.getBlue();
		    
		    float red = basec[0];
		    float blue = basec[2];
		    
		    
		    float num = red-blue;
		    float denom = red+blue;
		    
		    float ndvi;
		//    ndvi = basec[0];


            if (denom != 0.0F) {
                ndvi = num / denom;
            } else {
                ndvi = 0.0F;
            }

            if (ndvi < 0.0F) {
                ndvi = 0.0F;
            } else if (ndvi > 1.0F) {
                ndvi = 1.0F;
            }

		    
		    int ndvibyte = (int) (255*ndvi);
		    map.setRGB(x, y, ndvibyte);
//		    mapArray[x][y] = ndvibyte;	
//		    try {
//				outputStream.writeByte(ndvibyte);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    
		    

			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(map, "jpg", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			baos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		
		_logger.trace("Exiting Service: service1");
		return result;
	}

}
