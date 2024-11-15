package br.com.framework.hibertante.session;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.activation.DataSource;
import javax.faces.bean.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.SessionFactoryImplementor;


/**
 * Responsavel por estabelecer a conex�o com Hibernate
 * @author anailson
 *
 */
@ApplicationScoped
public class HibernateUtil implements Serializable  {
	
	private static final String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasouce";

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory = buildSessionFactory();
	

	/**
	 * Respons�vel por ler o arquivo de configura��o hibernate.cfg.xml
	 * @return SessionFactory
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				sessionFactory = (new Configuration()).configure()
						.buildSessionFactory();
			}
			return sessionFactory;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(
					"Erro ao criar conex�o SessionFactory");
		}
	}

	/**
	 * Retorna o sessionFactory corrente.
	 * @return sessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Retonar a sess�o do SessionFactory
	 * @return Session
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 *  Abre uma nova sess�o no SessionFactory
	 * @return Session
	 */
	public static Session openSession() {
		if (sessionFactory == null)
			buildSessionFactory();
		return sessionFactory.openSession();
	}

	/**
	 * Obtem a conection do provedor de conex�es configurado	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnectionProvider() throws SQLException {
		return ((SessionFactoryImplementor) sessionFactory)
				.getConnectionProvider().getConnection();
	}

	/**
	 * 
	 * @return Connection no InitialContext java:/comp/env/jdbc/datasouce
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		InitialContext context = new InitialContext();
		DataSource ds = (DataSource) (DataSource) context
				.lookup(JAVA_COMP_ENV_JDBC_DATA_SOURCE);
		return ((HibernateUtil) ds).getConnection();
	}

	/**
	 * 
	 * @return DataSource JNDI tomcat
	 * @throws NamingException
	 */
	public DataSource getDataSourceJndi() throws NamingException {
		InitialContext context = new InitialContext();
		return (DataSource) context
				.lookup(br.com.framework.implementacao.crud.VariavelConexaoUtil.JAVA_COMP_ENV_JDBC_DATA_SOURCE);
	}



	
}
