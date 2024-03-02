package com.assetsense.assetsoft.ui;

import com.assetsense.assetsoft.ui.MyTreeResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyTreeResources extends ClientBundle {
	MyTreeResources INSTANCE = GWT.create(MyTreeResources.class);

	@Source("up-arrow.png")
	@ImageResource.ImageOptions(width = 13, height = 13)
	ImageResource treeOpen();

	@Source("down-arrow.png")
	@ImageResource.ImageOptions(width = 16, height = 16)
	ImageResource treeClosed();

	@Source("down-arrow.png")
	@ImageResource.ImageOptions(width = 16, height = 16)
	ImageResource treeLeaf();
}
