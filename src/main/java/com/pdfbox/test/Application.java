package com.pdfbox.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.pdfbox.pdf.util.AnchorTextLocatorService;
import com.pdfbox.pdf.util.TextAnchorLocation;

public class Application {


	/**
	 * This will print the documents data.
	 *
	 * @param args
	 *            The command line arguments.
	 *
	 * @throws Exception
	 *             If there is an error parsing the document.
	 */
	public static void main(String[] args) throws Exception {
		String filename = "test.pdf";
		if (args.length > 1) {
			filename = args[1];
		}
		
		PDDocument document = PDDocument.load(filename);
		if (document.isEncrypted()) {
			document.decrypt("");
		}
		
		// Just do it with the first page for now.
		PDPage page = (PDPage)document.getDocumentCatalog().getAllPages().get(0);
		BufferedImage bi = page.convertToImage();
			
		AnchorTextLocatorService ats = new AnchorTextLocatorService(filename);
		
		for (Entry<String, TextAnchorLocation> anchor : ats.getAnchors().entrySet()) {
			System.out.println(anchor.getKey() + " => " + anchor.getValue());
			
			Graphics2D g = (Graphics2D)bi.getGraphics();
			g.setColor(Color.RED);
			Rectangle box = anchor.getValue().getBoundingBox();
			g.drawRect(box.x, box.y, box.width, box.height);
		}
		
		ImageIO.write(bi, "png", new File("test.png"));
	}
}