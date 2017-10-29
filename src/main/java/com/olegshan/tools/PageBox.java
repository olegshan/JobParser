package com.olegshan.tools;

public class PageBox {

	private static final int BUTTONS_TO_SHOW = 5;

	private int totalPages;
	private int currentPage;
	private int firstPage;
	private int lastPage;

	public PageBox(int totalPages, int currentPage) {

		this.totalPages = totalPages;
		this.currentPage = currentPage;
	}

	public PageBox getPageBox() {
		int halfBoxSize = BUTTONS_TO_SHOW / 2;

		if (totalPages <= BUTTONS_TO_SHOW) {
			setFirstPage(1);
			setLastPage(totalPages);

		} else if (currentPage - halfBoxSize <= 0) {
			setFirstPage(1);
			setLastPage(BUTTONS_TO_SHOW);

		} else if (currentPage + halfBoxSize == totalPages) {
			setFirstPage(currentPage - halfBoxSize);
			setLastPage(totalPages);

		} else if (currentPage + halfBoxSize > totalPages) {
			setFirstPage(totalPages - BUTTONS_TO_SHOW + 1);
			setLastPage(totalPages);

		} else {
			setFirstPage(currentPage - halfBoxSize);
			setLastPage(currentPage + halfBoxSize);
		}

		return this;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
}
