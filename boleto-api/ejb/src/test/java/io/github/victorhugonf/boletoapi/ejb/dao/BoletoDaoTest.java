package io.github.victorhugonf.boletoapi.ejb.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.ejb.entity.Boleto;
import io.github.victorhugonf.boletoapi.ejb.utils.Factory;
import junit.framework.AssertionFailedError;

public class BoletoDaoTest extends EasyMockSupport{

	@TestSubject
	private BoletoDao boletoDao = new BoletoDao();
	
	@Mock
	private EntityManager entityManagerMock;
	
	@Rule
	public EasyMockRule rule = new EasyMockRule(this);

//	protected CriteriaBuilder getCriteriaBuilder(){
//		return getEntityManager().getCriteriaBuilder();
//	}
//
//	protected CriteriaQuery<E> createQuery(){
//		return getCriteriaBuilder().createQuery(getClazz());
//	}
//
//	protected Root<E> createRoot(CriteriaQuery<E> criteriaQuery){
//		return criteriaQuery.from(getClazz());
//	}
	
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

	//TODO: falta esse
//	public List<E> getAll() throws Exception {
//    	CriteriaQuery<E> cq = createQuery();
//    	cq.select(createRoot(cq));
//    	return getEntityManager().createQuery(cq).getResultList();
//    }

}