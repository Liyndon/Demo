package com.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5423004127704172656L;

	private Integer id;

    private String userName;

    private String userPassword;
    
    private String name;

    private Date cdate;

    private Date mdate;

    private Integer state;
    
    private Integer userType;
    
    private String loginIp;
    
    private String oldLoginIp;
    
    private Date loginDate;
    
    private Date oldLoginDate;
    
    private Set<Role> roles=new HashSet<>();
    
    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name="user_type")
    public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	  @Column(name="user_name")
	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
    @Column(name="user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }
    @Column(name="cdate")
    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
    @Column(name="mdate")
    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }
    @Column(name="state")
    public Integer getState() {
        return state;
    }
    @Column
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(Integer state) {
        this.state = state;
    }
    @Column(name="login_ip")
	public String getLoginIp() {
		return loginIp;
	}
    @Column(name="login_date")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	@Column(name="old_login_ip")
	public String getOldLoginIp() {
		return oldLoginIp;
	}
	@Column(name="old_login_date")
	public Date getOldLoginDate() {
		return oldLoginDate;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}
	
	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_role",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns=@JoinColumn(name="role_id"))
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
    
    
}