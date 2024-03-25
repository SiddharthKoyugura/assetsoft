package com.assetsense.assetsoft.ui;

import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;

public class DtoToDao {
	public Product convertToProductDao(ProductDTO productDTO) {
		Product product = new Product();

		product.setProductId(productDTO.getProductId());
		product.setName(productDTO.getName());

		if (product.getParentProduct() != null) {
			product.setParentProduct(convertToProductDao(productDTO.getParentProductDTO()));
		}

		return product;
	}

	public Module convertToModuleDao(ModuleDTO moduleDTO) {
		Module module = new Module();

		module.setModuleId(moduleDTO.getModuleId());
		module.setName(moduleDTO.getName());
		module.setProduct(convertToProductDao(moduleDTO.getProductDTO()));

		if (moduleDTO.getParentModuleDTO() != null) {
			module.setParentModule(convertToModuleDao(moduleDTO.getParentModuleDTO()));
		}

		return module;
	}

	public User convertToUserDao(UserDTO userDTO) {
		User user = new User();
		user.setUserId(userDTO.getUserId());
		user.setEmail(userDTO.getEmail());
		user.setName(userDTO.getName());
		user.setPassword(userDTO.getPassword());
		if (userDTO.getTeams() != null && userDTO.getTeams().size() > 0) {
			for (TeamDTO team : userDTO.getTeams()) {
				user.getTeams().add(convertToTeamDao(team));
			}
		}
		return user;
	}

	public Team convertToTeamDao(TeamDTO teamDTO) {
		Team team = new Team();
		team.setTeamId(teamDTO.getTeamId());
		team.setName(teamDTO.getName());
		return team;
	}
}
