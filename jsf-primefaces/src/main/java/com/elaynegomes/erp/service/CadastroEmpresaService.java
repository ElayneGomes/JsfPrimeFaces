package com.elaynegomes.erp.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.elaynegomes.erp.model.Empresa;
import com.elaynegomes.erp.repository.Empresas;
import com.elaynegomes.erp.util.Transacional;

public class CadastroEmpresaService implements Serializable {

	private static final long serialVersionUID = -4431877177041987895L;

	@Inject
	private Empresas empresas;
	
	@Transacional
	public void salvar(Empresa empresa) {
		empresas.salvar(empresa);
	}
	
	@Transacional
	public void excluir(Empresa empresa) {
		empresas.remover(empresa);
	}
	

}