package com.pdfbox.pdf.util;

import java.awt.Rectangle;

public class TextAnchorLocation {
	protected Rectangle boundingBox;
	protected Integer pageNumber;
	public TextAnchorLocation(Rectangle boundingBox, Integer pageNumber) {
		super();
		this.boundingBox = boundingBox;
		this.pageNumber = pageNumber;
	}
	
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return "TextAnchorLocation [boundingBox=" + boundingBox
				+ ", pageNumber=" + pageNumber + "]";
	}
}
