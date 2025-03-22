package br.com.foods.teal.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import br.com.foods.teal.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa os usuários cadastrados nos sistema
 * 
 * @author Caio Pereira Leal
 */
@Entity
@Table(name = "tb_user")
@Audited
@AuditTable(value = "tb_audit_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "email")
	@NotAudited
	private String email;

	@Column(name = "phone")
	@NotAudited
	private String phone;

	@Column(name = "cpf")
	@NotAudited
	private String cpf;
	
	@Column(name = "create_date")
	private LocalDateTime createDate;
	
	@Column(name = "update_date")
	private LocalDateTime updateDate;

	/**
	 * Construtor Vazio
	 */
	public User() {
	}

	/**
	 * Cria Uma nova instância de usuario
	 * 
	 * @param id
	 *         identificador do usuário
	 *        
	 * @param name
	 *         nome do usuário
	 *        
	 * @param email
	 *         email do usuário
	 *         
	 * @param phone
	 *         telefone do usuário
	 *         
	 * @param cpf
	 *         cpf do usuário
	 */
	public User(String id, String name, String email, String phone, String cpf) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.cpf = cpf;
	}

	/**
	 * Retorna uma instância preenchida com a classe usuáro
	 * 
	 * @return novo usuário
	 */
	public User(UserDTO userDTO) {
		this.id = userDTO.id();
		this.name = userDTO.name();
		this.email = userDTO.email();
		this.phone = userDTO.phone();
		this.cpf = userDTO.cpf();
		this.createDate = userDTO.createDate();
		this.updateDate = userDTO.updateDate();
	}


	/**
	 * Retorna o identificador do usuário
	 * 
	 * @return o id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Define o identidicador do usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retorna o nome do usuário
	 * 
	 * @return o nome
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define o nome do usuário
	 * 
	 * @param name
	 *         nome do usuário
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retorna o email do usuário
	 * 
	 * @return o email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Define o email do usuário
	 * 
	 * @param email
	 *         email do usuário
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Retorna o telefone do usuário
	 * 
	 * @return o telefone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Define o telefone do usuário
	 * 
	 * @param phone
	 *         telefone do usuário
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Retorna o cpf do usuário
	 * 
	 * @return o cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * Define o cpf do usuário
	 * 
	 * @param cpf
	 *         cpf do usuário
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * Retorna a data de criação do usuário
	 * 
	 * @return createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * Define a data de criação do usuário
	 * 
	 * @param createDate
	 *         data de criação
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	/**
	 * Retorna a data de atualização do usuário
	 * 
	 * @return updateDate
	 */
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	/**
	 * Define a data de alteração do usuário
	 * 
	 * @param updateDate
	 *         data de alteração
	 */
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", cpf=" + cpf + "]";
	}
}
