package com.elaynegomes.erp.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.elaynegomes.erp.model.Empresa;
import com.elaynegomes.erp.model.RamoAtividade;
import com.elaynegomes.erp.model.TipoEmpresa;
import com.elaynegomes.erp.repository.Empresas;
import com.elaynegomes.erp.repository.RamoAtividades;
import com.elaynegomes.erp.service.CadastroEmpresaService;
import com.elaynegomes.erp.util.FacesMessages;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

	private static final long serialVersionUID = -5829895127806915113L;

	@Inject
	private Empresas empresas;

	@Inject
	private RamoAtividades ramoAtividades;

	@Inject
	private CadastroEmpresaService cadastroEmpresaService;

	@Inject
	private FacesMessages messages;

	private List<Empresa> listaEmpresas;

	private String termoPesquisa;

	private Converter<RamoAtividade> ramoAtividadeConverter;

	private Empresa empresa;

	public void prepararNovaEmpresa() {
		empresa = new Empresa();

	}

	public void prepararEdicao() {
		ramoAtividadeConverter = new RamoAtividadeConverter(Arrays.asList(empresa.getRamoAtividade()));
	}

	public void salvar() {
		cadastroEmpresaService.salvar(empresa);
		atualizarRegistro();
		messages.info("Empresa Salva com sucesso!!!");

		PrimeFaces.current().ajax().update(Arrays.asList("frm:empresasDataTable", "frm:messages"));
	}

	public void excluir() {
		cadastroEmpresaService.excluir(empresa);

		empresa = null;
		atualizarRegistro();
		messages.info("Empresa excluída com sucesso!!!");
	}

	private void atualizarRegistro() {
		if (jaHouvePesquisa()) {
			pesquisar();
		} else {
			todasEmpresas();
		}
	}

	private boolean jaHouvePesquisa() {
		return termoPesquisa != null && !"".equals(termoPesquisa);
	}

	public void pesquisar() {
		listaEmpresas = empresas.pesquisar(termoPesquisa);
		if (listaEmpresas.isEmpty()) {
			messages.info("Sua consulta não retornou registros...");
		}
	}

	public void todasEmpresas() {
		listaEmpresas = empresas.todas();
	}

	public List<RamoAtividade> completarRamoAtividade(String termo) {

		List<RamoAtividade> listaRamoAtividades = ramoAtividades.pesquisar(termo);

		ramoAtividadeConverter = new RamoAtividadeConverter(listaRamoAtividades);
		return listaRamoAtividades;
	}

	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	public String getTermoPesquisa() {
		return termoPesquisa;
	}

	public void setTermoPesquisa(String termoPesquisa) {
		this.termoPesquisa = termoPesquisa;
	}

	public TipoEmpresa[] getTiposEmpresa() {
		return TipoEmpresa.values();
	}

	public Converter<RamoAtividade> getRamoAtividadeConverter() {
		return ramoAtividadeConverter;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public boolean isEmpresaSelecionada() {
		return empresa != null && empresa.getId() != null;
	}
}