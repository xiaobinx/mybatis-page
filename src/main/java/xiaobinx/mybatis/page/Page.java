package xiaobinx.mybatis.page;

public class Page {

	private int pageSize = 15;

	private int p = 1;

	private int totalPage = 1;

	private int totalSize;

	public Page() {
	}

	public Page(int p) {
		this.p = p;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getP() {
		if (p < 1) {
			p = 1;
		}
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		if (this.totalSize % this.pageSize != 0) {
			this.totalPage = (int) (this.totalSize / this.pageSize + 1);
		} else {
			this.totalPage = (int) (this.totalSize / this.pageSize);
		}
		if (totalPage == 0) {
			totalPage = 1;
		}
		this.totalSize = totalSize;
	}

	@Override
	public String toString() {
		return "Page [pageSize=" + pageSize + ", p=" + p + ", totalPage=" + totalPage + ", totalSize=" + totalSize
				+ "]";
	}

}