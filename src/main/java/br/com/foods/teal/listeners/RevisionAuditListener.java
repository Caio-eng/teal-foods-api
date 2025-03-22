package br.com.foods.teal.listeners;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import br.com.foods.teal.model.HistoryInformation;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe para Controle da Auditoria
 * 
 * @author Caio Pereira Leal
 */
public class RevisionAuditListener implements RevisionListener {
	private static final Log log = LogFactory.getLog( RevisionAuditListener.class.getName() );

	/**
	 * Responsável por preencher o histórico
	 */
	@Override
	public void newRevision(Object revisionEntity) {
		HistoryInformation informacaoHistorico = (HistoryInformation) revisionEntity;
		try {
			preencherInformacaoHistorico( informacaoHistorico );
		} catch ( Exception e ) {
			log.error( "Erro ao capturar informações do usuário", e );
		}
	}

	private void preencherInformacaoHistorico(HistoryInformation informacaoHistorico) {
		
		String ip = getIpClient();

		String user = getUser();

		String origin = getOrigin();

		informacaoHistorico.setIp( ip );
		informacaoHistorico.setUser( user );
		informacaoHistorico.setOriginAlt( origin );
		informacaoHistorico.setUpdateDate( LocalDateTime.now() );

		log.info( "Histórico preenchido: " + informacaoHistorico );
	}

	private String getIpClient() {
		return Optional.ofNullable( RequestContextHolder.getRequestAttributes() )
				.map( requestAttributes -> (HttpServletRequest) requestAttributes
						.resolveReference( RequestAttributes.REFERENCE_REQUEST ) )
				.map( this::extrairIpReal ).orElse( "IP DESCONHECIDO" );
	}

	private String extrairIpReal(HttpServletRequest request) {
		String ip = request.getHeader( "X-Forwarded-For" );
		if ( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
			ip = request.getHeader( "Proxy-Client-IP" );
		}
		if ( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
			ip = request.getHeader( "WL-Proxy-Client-IP" );
		}
		if ( ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase( ip ) ) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String getUser() {
		return Optional.ofNullable( SecurityContextHolder.getContext().getAuthentication() )
				.map( Authentication::getName ).orElseGet( () -> {
					return Optional.ofNullable( RequestContextHolder.getRequestAttributes() )
							.map( requestAttributes -> (String) requestAttributes.getAttribute( "usuario",
									RequestAttributes.SCOPE_REQUEST ) )
							.orElse( "Usuário desconhecido" );
				} );
	}

	private String getOrigin() {
		return Optional.ofNullable( RequestContextHolder.getRequestAttributes() )
				.map( requestAttributes -> (String) requestAttributes.getAttribute( "origem",
						RequestAttributes.SCOPE_REQUEST ) )
				.orElse( "Origem desconhecida" );
	}
}