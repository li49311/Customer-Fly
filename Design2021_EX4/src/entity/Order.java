package entity;

import java.util.Date;

public class Order {
	
	int OrderNum;
	Date OrderDate;
	util.PamentMethod PamentMethod;
	
	
	public Order(int orderNum, Date orderDate, util.PamentMethod pamentMethod) {
		super();
		OrderNum = orderNum;
		OrderDate = orderDate;
		PamentMethod = pamentMethod;
	}
	
	public int getOrderNum() {
		return OrderNum;
	}
	public void setOrderNum(int orderNum) {
		OrderNum = orderNum;
	}
	public Date getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(Date orderDate) {
		OrderDate = orderDate;
	}
	public util.PamentMethod getPamentMethod() {
		return PamentMethod;
	}
	public void setPamentMethod(util.PamentMethod pamentMethod) {
		PamentMethod = pamentMethod;
	}

	@Override
	public String toString() {
		return "Order [OrderNum=" + OrderNum + ", OrderDate=" + OrderDate + ", PamentMethod=" + PamentMethod + "]";
	}
	
	

}
