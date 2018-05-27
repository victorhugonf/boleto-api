package io.github.victorhugonf.boletoapi.ejb.entity;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.victorhugonf.boletoapi.ejb.useful.Factory;

public class BoletoTest{
	
	private static Validator validator;

	@BeforeClass
    public static void setUpClass() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
	
	@Test
	public void validarBoleto(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertTrue(constraintViolations.isEmpty());
	}
	
	@Test
	public void validarStatusNulo(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setStatus(null);
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void validarDataVencimentoNula(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setDataVencimento(null);
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void validarNomeClienteNulo(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setNomeCliente(null);
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void validarNomeClienteVazio(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setNomeCliente("");
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}

	@Test
	public void validarValorTotalEmCentavosNulo(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setValorTotalEmCentavos(null);
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void validarValorTotalEmCentavosZerado(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setValorTotalEmCentavos(BigDecimal.ZERO);
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}
	
	@Test
	public void validarValorTotalEmCentavosFracionado(){
		Boleto boleto = Factory.createBoletoPendenteFake();
		boleto.setValorTotalEmCentavos(BigDecimal.valueOf(1.1));
		
		Set<ConstraintViolation<Boleto>> constraintViolations = validator.validate(boleto);

		Assert.assertEquals(1, constraintViolations.size());
	}

}
