package com.assetsense.assetsoft.ui;

import com.assetsense.assetsoft.ui.MyTreeResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyTreeResources extends ClientBundle {
	MyTreeResources INSTANCE = GWT.create(MyTreeResources.class);

	@Source("down-arrow.png")
	@ImageResource.ImageOptions(width = 16, height = 16)
	ImageResource treeOpen();

	@Source("right-icon.png")
	@ImageResource.ImageOptions(width = 12, height = 12)
	ImageResource treeClosed();

	@Source("right-icon.png")
	@ImageResource.ImageOptions(width = 12, height = 12)
	ImageResource treeLeaf();
}
