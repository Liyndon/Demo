package com.demo.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="permission")
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5721358905187780110L;

	private Integer id;

	private Resource resources;

	private Integer order;

	private Integer state;
	
	private Permission permissionInfo;

	private Set<Role> roles = new HashSet<>();

	private Set<Permission> permissions = new HashSet<>();

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "resource_id")
	public Resource getResources() {
		return resources;
	}

	public void setResources(Resource resources) {
		this.resources = resources;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public Permission getPermissionInfo() {
		return permissionInfo;
	}

	public void setPermissionInfo(Permission permissionInfo) {
		this.permissionInfo = permissionInfo;
	}

	@OneToMany(mappedBy = "permissionInfo", cascade = { javax.persistence.CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderBy("order")
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@ManyToMany(mappedBy = "permissions", cascade = CascadeType.PERSIST)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}