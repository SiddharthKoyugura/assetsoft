package com.assetsense.assetsoft.ui;

import java.util.List;

import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.service.ProductService;
import com.assetsense.assetsoft.service.ProductServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddProduct {
	private final ProductServiceAsync productService = GWT.create(ProductService.class);
	
	private ListBox productField;
	private TextBox newProductField;
	private Button submitBtn;

	public ListBox getProductField() {
		return productField;
	}

	public TextBox getNewProductField() {
		return newProductField;
	}

	public void setSubmitBtnHandler(ClickHandler handler) {
		submitBtn.addClickHandler(handler);
	}

	public VerticalPanel buildAddProductForm() {
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setStyleName("form-container");
		vpanel.setWidth("100%");

		Label l1 = new Label("Select Parent:");
		l1.setStyleName("mr-5");
		l1.addStyleName("taskLabel");
		
		productField = new ListBox();
		productField.setStyleName("listBoxStyle");
		productField.addItem("NULL");
		

		Label l2 = new Label("New Product:");
		l2.setStyleName("mr-5");
		l2.addStyleName("taskLabel");
		newProductField = new TextBox();
		newProductField.setStyleName("listBoxStyle");
		
		
		submitBtn = new Button("Submit");
		submitBtn.setStyleName("customBtn");

		final Grid grid = new Grid(5, 2);
		grid.setCellPadding(10);
		grid.getElement().getStyle().setProperty("margin", "100px 0 0 0");
		grid.getElement().getStyle().setProperty("borderCollapse", "collapse");
		grid.setWidth("100%");

		grid.setWidget(0, 0, l1);
		grid.setWidget(1, 0, l2);

		
		grid.setWidget(1, 1, newProductField);
		grid.setWidget(3, 1, submitBtn);
		
		productService.getProducts(new AsyncCallback<List<ProductDTO>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(List<ProductDTO> products) {
				// TODO Auto-generated method stub
				for(ProductDTO product: products){
					String topMostParentName = product.findTopMostParent().getName();
					String childName = product.getName();
					if(topMostParentName.equals(childName)){
						productField.addItem(childName);
					}else{
						productField.addItem(topMostParentName + " >> " + childName);
					}
				}
				grid.setWidget(0, 1, productField);
			}
			
		});

		grid.getCellFormatter().setStyleName(0, 0, "text-right");
		grid.getCellFormatter().setStyleName(1, 0, "text-right");
		grid.getCellFormatter().setStyleName(2, 0, "text-right");

		grid.getCellFormatter().getElement(3, 1).getStyle().setProperty("textAlign", "left");

		vpanel.add(grid);
		return vpanel;
	}
}
