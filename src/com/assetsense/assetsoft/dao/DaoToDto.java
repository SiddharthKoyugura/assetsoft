package com.assetsense.assetsoft.dao;

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

public class DaoToDto {
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
		
		if(task.getSubSystem() != null){
			taskDTO.setSubSystem(convertToModuleDTO(task.getSubSystem()));
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
}
