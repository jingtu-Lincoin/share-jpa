package net.ahzz.share.sharejpa.bean;

import lombok.Data;

import java.util.List;


/**
 * 页面的page对象
 * 
 * @author 呼唤
 * 
 */
@Data
public class Page<T> {

	public long totalCounts;// 总记录数
	public int pages;// 总页数
	public int currentPage;// 当前页
	public int toPage; // 转到哪一页
	public int pageSize;// 每页显示条数
	public int startRow; // 开始行号
	public List<T> list;

	public Page(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public Page() {
	}

	public long getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(long totalCounts) {
		this.totalCounts = totalCounts;
	}

	public int getStartRow() {
		startRow = (this.currentPage - 1) * this.pageSize;
		return startRow;
	}



}
