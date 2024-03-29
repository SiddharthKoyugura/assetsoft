package com.assetsense.assetsoft.util;

import java.util.HashSet;
import java.util.Set;

import com.assetsense.assetsoft.domain.Lookup;
import com.assetsense.assetsoft.domain.Module;
import com.assetsense.assetsoft.domain.Product;
import com.assetsense.assetsoft.domain.Task;
import com.assetsense.assetsoft.domain.Team;
import com.assetsense.assetsoft.domain.User;
import com.assetsense.assetsoft.dto.LookupDTO;
import com.assetsense.assetsoft.dto.ModuleDTO;
import com.assetsense.assetsoft.dto.ProductDTO;
import com.assetsense.assetsoft.dto.TaskDTO;
import com.assetsense.assetsoft.dto.TeamDTO;
import com.assetsense.assetsoft.dto.UserDTO;

public class TypeConverter {
	// Dao To Dto
	public TaskDTO convertToTaskDTO(Task task) {
		TaskDTO taskDTO = new TaskDTO();

		taskDTO.setTaskId(task.getTaskId());

		taskDTO.setTitle(task.getTitle());

		if (task.getType() != null) {
			taskDTO.setType(convertToLookupDTO(task.getType()));
		}
		taskDTO.setDescription(task.getDescription());

		taskDTO.setInitialEstimate(task.getInitialEstimate());

		taskDTO.setPercentComplete(task.getPercentComplete());

		taskDTO.setRemainingEstimate(task.getRemainingEstimate());
		
		taskDTO.setStartDate(task.getStartDate());

		taskDTO.setDueDate(task.getDueDate());

		if (task.getPriority() != null) {
			taskDTO.setPriority(convertToLookupDTO(task.getPriority()));
		}

		if (task.getStatus() != null) {
			taskDTO.setStatus(convertToLookupDTO(task.getStatus()));
		}

		if (task.getUser() != null) {
			taskDTO.setUser(convertToUserDTO(task.getUser()));
		}

		if (task.getProduct() != null) {
			taskDTO.setProduct(convertToProductDTO(task.getProduct()));
		}

		if (task.getModule() != null) {
			taskDTO.setModule(convertToModuleDTO(task.getModule()));
		}

		return taskDTO;
	}

	public LookupDTO convertToLookupDTO(Lookup lookup) {
		LookupDTO lookupDTO = new LookupDTO();

		lookupDTO.setLookupId(lookup.getLookupId());
		lookupDTO.setCatId(lookup.getLookupId());
		lookupDTO.setValue(lookup.getValue());

		return lookupDTO;
	}

	public UserDTO convertToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();

		userDTO.setUserId(user.getUserId());
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getEmail());
		userDTO.setPassword(user.getPassword());
		// Convert teams to TeamDTOs
		if (user.getTeams() != null) {
			userDTO.setTeams(convertTeamsToDTOs(user.getTeams()));
		}

		return userDTO;
	}

	public Set<TeamDTO> convertTeamsToDTOs(Set<Team> teams) {
		if (teams != null && !teams.isEmpty()) {
			Set<TeamDTO> teamDTOs = new HashSet<>();
			for (Team team : teams) {
				TeamDTO teamDTO = new TeamDTO();
				teamDTO.setTeamId(team.getTeamId());
				teamDTO.setName(team.getName());
				teamDTOs.add(teamDTO);
			}
			return teamDTOs;
		} else {
			return null;
		}
	}

	public ProductDTO convertToProductDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();

		productDTO.setProductId(product.getProductId());

		productDTO.setName(product.getName());

		if (product.getParentProduct() != null) {
			productDTO.setParentProductDTO(convertToProductDTO(product.getParentProduct()));
		}

		return productDTO;
	}

	public ModuleDTO convertToModuleDTO(Module module) {
		ModuleDTO moduleDTO = new ModuleDTO();

		moduleDTO.setModuleId(module.getModuleId());
		moduleDTO.setName(module.getName());
		moduleDTO.setProductDTO(convertToProductDTO(module.getProduct()));

		if (module.getParentModule() != null) {
			moduleDTO.setParentModuleDTO(convertToModuleDTO(module.getParentModule()));
		}

		return moduleDTO;
	}

	// DTO to DAO
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
