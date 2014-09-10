package com.pdfbox.pdf.util;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;

import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class AnchorTextRipper extends PDFTextStripper {
	protected enum ScanState {
		START,
		SEARCHING,
		FOUND_POSSIBLE,
		SCANNING_ANCHOR,
		DONE
	}
	
	protected Integer currentPage = 0;
	protected HashMap<String, TextAnchorLocation> anchors = new HashMap<String, TextAnchorLocation>();
	
	// Scanning variables
	protected ScanState state = ScanState.START;
	protected TextPosition lastFoundAnchor;
	protected StringBuilder lastFoundAnchorText;
	protected Double lastWidth;
	protected Rectangle lastFoundAnchorRect;

	public AnchorTextRipper() throws IOException {
		super();
		this.setSortByPosition(true);
	}
	
	/**
	 * A method provided as an event interface to allow a subclass to perform
	 * some specific functionality when text needs to be processed.
	 *
	 * @param text
	 *            The text to be processed
	 */
	@Override
	protected void processTextPosition(TextPosition text) {
		switch(state) {
		case START:
			state = ScanState.SEARCHING;
			lastFoundAnchor = null;
			lastFoundAnchorText = new StringBuilder();
			lastWidth = 0.0;
			lastFoundAnchorRect = null;
			break;
		case SEARCHING:
			if (text.getCharacter().equals("$")) {
				state = ScanState.FOUND_POSSIBLE;
				lastFoundAnchor = text;
			}
			break;
		case FOUND_POSSIBLE:
			if (text.getCharacter().equals("{")) {
				state = ScanState.SCANNING_ANCHOR;
			} else {
				state = ScanState.START;
			}
			break;
		case SCANNING_ANCHOR:
			if (text.getCharacter().equals("}")) {
				state = ScanState.DONE;
				break;
			}
			
			if (!text.getCharacter().equals(">")) {
				lastFoundAnchorText.append(text.getCharacter());
			}
			break;
		case DONE:
			//System.out.println(String.format("%f, %f (%f, %f) [%f, %f]", lastFoundAnchor.getX(), lastFoundAnchor.getY(), lastFoundAnchor.getXScale(), lastFoundAnchor.getYScale(), lastFoundAnchor.getWidth(), lastFoundAnchor.getHeight()));
			lastFoundAnchorRect = new Rectangle((int)Math.round(lastFoundAnchor.getX()) , (int)Math.round((lastFoundAnchor.getY()) - lastFoundAnchor.getHeight()), (int)Math.round(lastWidth), (int)Math.round(lastFoundAnchor.getHeight()));
			anchors.put(lastFoundAnchorText.toString(), new TextAnchorLocation(lastFoundAnchorRect, currentPage));
			state = ScanState.START;
			break;
		}
		
		if (state != ScanState.SEARCHING) {
			lastWidth += text.getWidth();
		}
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
}
