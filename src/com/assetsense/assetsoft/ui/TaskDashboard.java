package com.assetsense.assetsoft.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskDashboard {
	
	Tree.Resources customTreeResources = new Tree.Resources() {
        @Override
        public ImageResource treeClosed() {
            return MyTreeResources.INSTANCE.treeClosed();
        }

        @Override
        public ImageResource treeLeaf() {
            return MyTreeResources.INSTANCE.treeLeaf();
        }

        @Override
        public ImageResource treeOpen() {
            return MyTreeResources.INSTANCE.treeOpen();
        }
    };

	public HorizontalPanel buildNavBar() {
		HorizontalPanel navbar = new HorizontalPanel();
		navbar.setWidth("100%");

		Label l1 = new Label("Assetsoft");
		l1.setStyleName("navLabel");

		Button btn = new Button("Sai kiran");
		btn.setStyleName("navBtn");

		navbar.add(l1);
		navbar.add(btn);

		// Style the navbar
		navbar.setStyleName("navbar");
		return navbar;
	}

	private Tree buildProductsTree() {
		Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");

		TreeItem mainItem = new TreeItem();
		mainItem.setText("Products");
		mainItem.setStyleName("treeHeading");

		TreeItem item1 = new TreeItem();
		item1.setText("A2");
		TreeItem item2 = new TreeItem();
		item2.setText("C2");

		TreeItem sub1 = new TreeItem();
		sub1.setText("ML Sprint");
		TreeItem sub2 = new TreeItem();
		sub2.setText("RAG Sprint");

		item1.addItem(sub1);
		item1.addItem(sub2);
		item1.setState(true);

		TreeItem sub21 = new TreeItem();
		sub21.setText("6.1");
		TreeItem sub22 = new TreeItem();
		sub22.setText("6.2");
		TreeItem sub23 = new TreeItem();
		sub23.setText("6.3");
		TreeItem sub24 = new TreeItem();
		sub24.setText("6.4");
		TreeItem sub25 = new TreeItem();
		sub25.setText("6.5");
		TreeItem sub26 = new TreeItem();
		sub26.setText("6.6");
		
		TreeItem sub31 = new TreeItem();
		sub31.setText("Sprint 1");
		TreeItem sub32 = new TreeItem();
		sub32.setText("Sprint 2");
		
		sub25.addItem(sub31);
		sub25.addItem(sub32);
		sub25.setState(true);
		
		item2.addItem(sub21);
		item2.addItem(sub22);
		item2.addItem(sub23);
		item2.addItem(sub24);
		item2.addItem(sub25);
		item2.addItem(sub26);
		
		item2.setState(true);

		mainItem.addItem(item1);
		mainItem.addItem(item2);
		mainItem.setState(true);
		
		tree.addItem(mainItem);
		return tree;
	}
	
	private Tree buildUsersTree() {
		Tree tree = new Tree(customTreeResources);

		tree.setStyleName("parentTree");

		TreeItem mainItem = new TreeItem();
		mainItem.setText("Users & Teams");
		mainItem.setStyleName("treeHeading");

		TreeItem item1 = new TreeItem();
		item1.setText("All Users");
		TreeItem item2 = new TreeItem();
		item2.setText("All Teams");

		TreeItem sub1 = new TreeItem();
		sub1.setText("Siddhardha Koyugura");
		TreeItem sub2 = new TreeItem();
		sub2.setText("Goutham Mandala");

		item1.addItem(sub1);
		item1.addItem(sub2);
		item1.setState(true);

		TreeItem sub21 = new TreeItem();
		sub21.setText("Frontend Team");
		TreeItem sub22 = new TreeItem();
		sub22.setText("Backend Team");

		item2.addItem(sub21);
		item2.addItem(sub22);
		item2.setState(true);

		mainItem.addItem(item1);
		mainItem.addItem(item2);
		mainItem.setState(true);

		tree.addItem(mainItem);
		return tree;
	}
	
	public VerticalPanel buildLeftSidebar() {
		VerticalPanel vpanel = new VerticalPanel();
		
		vpanel.setStyleName("leftSidebar");
		vpanel.add(buildProductsTree());
		HTML hrLine = new HTML("<div class='hr' />");
		vpanel.add(hrLine);
		vpanel.add(buildUsersTree());
		
		return vpanel;
	}
}
