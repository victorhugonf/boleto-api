package io.github.victorhugonf.boletoapi.ejb.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.entity.StatusEnum;
import io.github.victorhugonf.boletoapi.ejb.utils.Factory;
import junit.framework.AssertionFailedError;

public class BoletoDaoTest extends EasyMockSupport{

	@TestSubject
	private BoletoDao boletoDao = new BoletoDao();
	
	@Mock
	private EntityManager entityManagerMock;
	
	@Mock
	private CriteriaBuilder criteriaBuilderMock;
	
	@Mock
	private CriteriaQuery<Boleto> criteriaQueryMock;
	
	@Mock
	private Root<Boleto> rootMock;
	
	@Mock
	private TypedQuery<Boleto> typedQueryMock;
	
	@Rule
	public EasyMockRule rule = new EasyMockRule(this);
	
	@Test
	public void persist_persistirBoleto() throws Exception{
		Boleto boleto = Factory.createBoleto();
		
		entityManagerMock.persist(boleto);
		EasyMock.expectLastCall();
		replayAll();
		
		Boleto boletoPersistido = boletoDao.persist(boleto);
		
		Assert.assertNotNull(boletoPersistido);
		Assert.assertEquals(boleto, boletoPersistido);
		verifyAll();
	}
	
	@Test
	public void merge_persistirBoleto() throws Exception{
		Boleto boleto = Factory.createBoleto();
		
		EasyMock.expect(entityManagerMock.merge(boleto)).andReturn(boleto);
		replayAll();
		
		Boleto boletoPersistido = boletoDao.merge(boleto);
		
		Assert.assertNotNull(boletoPersistido);
		Assert.assertEquals(boleto, boletoPersistido);
		verifyAll();
	}

	@Test
	public void remove_removerBoleto() throws Exception{
		Boleto boleto = Factory.createBoleto();
		UUID uuid = Factory.createUuid();
		boleto.setId(uuid);
		
		EasyMock.expect(entityManagerMock.find(Boleto.class, uuid)).andReturn(boleto);
		entityManagerMock.remove(boleto);
		EasyMock.expectLastCall();
		replayAll();
		
		boletoDao.remove(boleto);
		
		verifyAll();
	}
	
	@Test
	public void getByObject_objetoNulo_retornarNulo() throws Exception{
		EasyMock.expect(entityManagerMock.find(Boleto.class, null)).andThrow(new AssertionFailedError()).anyTimes();
		replayAll();
		
		Boleto boletoRetornado = boletoDao.get(null);
		
		Assert.assertNull(boletoRetornado);
		verifyAll();
	}

	@Test
	public void getByObject_objetoValido_retornarObjeto() throws Exception{
		Boleto boleto = Factory.createBoleto();
		UUID uuid = Factory.createUuid();
		boleto.setId(uuid);
		
		EasyMock.expect(entityManagerMock.find(Boleto.class, uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoDao.get(boleto);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(boleto.getId(), boletoRetornado.getId());
		verifyAll();
	}
	
	@Test
	public void getById_uuidNulo_retornarNulo() throws Exception{
		EasyMock.expect(entityManagerMock.find(Boleto.class, null)).andReturn(null);
		replayAll();
		
		Boleto boletoRetornado = boletoDao.getById(null);
		
		Assert.assertNull(boletoRetornado);
		verifyAll();
	}
	
	@Test
	public void getById_uuidValido_retornarObjeto() throws Exception{
		Boleto boleto = Factory.createBoleto();
		UUID uuid = Factory.createUuid();
		boleto.setId(uuid);
		
		EasyMock.expect(entityManagerMock.find(Boleto.class, uuid)).andReturn(boleto);
		replayAll();
		
		Boleto boletoRetornado = boletoDao.getById(uuid);
		
		Assert.assertNotNull(boletoRetornado);
		Assert.assertEquals(boleto.getId(), boletoRetornado.getId());
		verifyAll();
	}

	@Test
	public void getAll_retornarRegistros() throws Exception{
		List<Boleto> boletos = new ArrayList<>();
		boletos.add(Factory.createBoleto(Factory.createUuid(), StatusEnum.PENDING, BigDecimal.TEN, Factory.createDateNow()));
		
		EasyMock.expect(entityManagerMock.getCriteriaBuilder()).andReturn(criteriaBuilderMock);
		EasyMock.expect(criteriaBuilderMock.createQuery(Boleto.class)).andReturn(criteriaQueryMock);
		EasyMock.expect(criteriaQueryMock.from(Boleto.class)).andReturn(rootMock);
		EasyMock.expect(criteriaQueryMock.select(rootMock)).andReturn(criteriaQueryMock);
		EasyMock.expect(entityManagerMock.createQuery(criteriaQueryMock)).andReturn(typedQueryMock);
		EasyMock.expect(typedQueryMock.getResultList()).andReturn(boletos);
		replayAll();
		
		List<Boleto> boletosRetornados = boletoDao.getAll();
		
		Assert.assertNotNull(boletosRetornados);
		Assert.assertEquals(boletos.size(), boletosRetornados.size());
		verifyAll();
	}
	
	@Test
	public void getAll_retornarListaVazia() throws Exception{
		List<Boleto> boletos = new ArrayList<>();
		
		EasyMock.expect(entityManagerMock.getCriteriaBuilder()).andReturn(criteriaBuilderMock);
		EasyMock.expect(criteriaBuilderMock.createQuery(Boleto.class)).andReturn(criteriaQueryMock);
		EasyMock.expect(criteriaQueryMock.from(Boleto.class)).andReturn(rootMock);
		EasyMock.expect(criteriaQueryMock.select(rootMock)).andReturn(criteriaQueryMock);
		EasyMock.expect(entityManagerMock.createQuery(criteriaQueryMock)).andReturn(typedQueryMock);
		EasyMock.expect(typedQueryMock.getResultList()).andReturn(boletos);
		replayAll();
		
		List<Boleto> boletosRetornados = boletoDao.getAll();
		
		Assert.assertNotNull(boletosRetornados);
		Assert.assertEquals(boletos.size(), boletosRetornados.size());
		verifyAll();
	}

}