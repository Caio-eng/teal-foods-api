package br.com.foods.teal.model;

import java.time.LocalDateTime;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import br.com.foods.teal.listeners.RevisionAuditListener;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe para armarzenar as informações da auditoria
 * 
 * @author Caio Pereira Leal
 */
@Entity
@Table(name = "history_information")
@RevisionEntity(RevisionAuditListener.class)
public class HistoryInformation {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long id;
 
    private String ip;
    private String user;
    
    private String originAlt;
 
    @RevisionTimestamp
    private LocalDateTime updateDate;
 
    /**
	 * Retorna o identificador do histórico
	 * 
	 * @return o id
	 */
    public Long getId() {
        return id;
    }
 
    /**
	 * Define o identidicador do usuário
	 * 
	 * @param id
	 *         identificador do usuário
	 */
    public void setId(Long id) {
        this.id = id;
    }
 
    /**
	 * Retorna a Data de Alteração do Histório
	 * 
	 * @return o data de alteração
	 */
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
 
    /**
	 * Define a Data de Alteração do Histório
	 * 
	 * @param updateDate
	 *         data da alteração do histórico
	 */
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
 
    /**
	 * Retorna o ip
	 * 
	 * @return ip
	 */
    public String getIp() {
        return ip;
    }
 
    /**
	 * Define o ip
	 * 
	 * @param ip
	 *         ip do usuário logado
	 */
    public void setIp(String ip) {
        this.ip = ip;
    }
 
    /**
	 * Retorna o usuário
	 * 
	 * @return user
	 */
    public String getUser() {
        return user;
    }
 
    /**
	 * Define o usuário
	 * 
	 * @param user
	 *         usuário
	 */
    public void setUser(String user) {
        this.user = user;
    }
 
    /**
	 * Retorna a origem da alteração
	 * 
	 * @return origin
	 */
    public String getOriginAlt() {
        return originAlt;
    }
 
    /**
	 * Define a origem da alteração
	 * 
	 * @param originAlt
	 *         origem da alteração
	 */
    public void setOriginAlt(String originAlt) {
        this.originAlt = originAlt;
    }
}
