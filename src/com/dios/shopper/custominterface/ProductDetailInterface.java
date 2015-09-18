package com.dios.shopper.custominterface;

public interface ProductDetailInterface {
	public void onCloseButtonClick(int position, int productID);
	public void onAddToWishlistClick(int position, int productID);
	public void onAddToBagClick(int position, int productID);
}
