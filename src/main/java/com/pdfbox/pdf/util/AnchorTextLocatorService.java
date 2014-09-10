package com.pdfbox.pdf.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;

public class AnchorTextLocatorService {
	protected AnchorTextRipper ripper = new AnchorTextRipper();

	public AnchorTextLocatorService(String filename) throws IOException {
		PDDocument document = null;

		try {
			document = PDDocument.load(filename);
			if (document.isEncrypted()) {
				document.decrypt("");
			}
			
			@SuppressWarnings("unchecked")
			List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
			
			for (int i = 0; i < allPages.size(); i++) {
				PDPage page = (PDPage) allPages.get(i);
				PDStream contents = page.getContents();
				ripper.setCurrentPage(i);
				if (contents != null) {
					ripper.processStream(page, page.findResources(), page.getContents().getStream());
				}
			}
		} catch (CryptographyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

	public HashMap<String, TextAnchorLocation> getAnchors() {
		return ripper.anchors;
	}
	
	public TextAnchorLocation getAnchorRect(String anchorText) {
		return ripper.anchors.get(anchorText);
	}
}