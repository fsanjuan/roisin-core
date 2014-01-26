package com.roisin.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rapidminer.operator.learner.rules.Rule;
import com.rapidminer.operator.learner.rules.RuleModel;

/**
 * Implementaci�n de todos los m�todos necesarios para la obtenci�n de
 * resultados a partir del algoritmo Ripper.
 * 
 * @author F�lix Miguel Sanju�n Segovia <fmsanse@gmail.com>
 * 
 */
public class RipperResultsImpl {

	/**
	 * Rule model a partir del cual se hallar�n los resultados.
	 */
	private RuleModel ruleModel;

	private int numCasos;

	private Map<Rule, Double> soportes;

	private Map<Rule, Double> precisiones;

	/**
	 * Constructor vac�o.
	 */
	public RipperResultsImpl() {
		throw new IllegalArgumentException(
				"El objeto Ripper results necesita un rule model, se est� llamando al constructor vac�o.");
	}

	/**
	 * Constructor p�blico.
	 * 
	 * @param ruleModel
	 */
	public RipperResultsImpl(RuleModel ruleModel) {
		this.ruleModel = ruleModel;
		this.numCasos = calculateNumCasos();
		this.soportes = populateSupportMap();
		this.precisiones = populateConfidenceMap();
	}

	/**
	 * Devuelve el ruleModel.
	 * 
	 * @return
	 */
	public RuleModel getRuleModel() {
		return ruleModel;
	}

	/**
	 * Calcula el n�mero total de casos contenidos en los datos de ejemplo. El
	 * c�lculo se realiza a partir de la frecuencia.
	 * 
	 * @return numCasos n�mero total de casos
	 */
	private int calculateNumCasos() {
		int numCasos = 0;
		for (Rule rule : ruleModel.getRules()) {
			int[] ruleFrequencies = rule.getFrequencies();
			numCasos += ruleFrequencies[0] + ruleFrequencies[1];
		}
		return numCasos;
	}

	/**
	 * Devuelve el n�mero total de casos contenidos en los datos de ejemplo. El
	 * c�lculo se realiza a partir de la frecuencia.
	 * 
	 * @return numCasos n�mero total de casos
	 */
	public int getNumCasos() {
		return this.numCasos;
	}

	/**
	 * Devuelve un mapa que contiene las reglas asociadas a su precisi�n.
	 * 
	 * @return Map<Rule, Double> mapa
	 */
	private Map<Rule, Double> populateConfidenceMap() {
		Map<Rule, Double> map = new HashMap<Rule, Double>();
		for (Rule rule : ruleModel.getRules()) {
			int labelIndex = getLabelNames().indexOf(rule.getLabel());
			map.put(rule, rule.getConfidences()[labelIndex]);
		}
		return map;
	}

	/**
	 * Devuelve un mapa que contiene las reglas asociadas a su soporte.
	 * 
	 * @return Map<Rule, Double> mapa
	 */
	private Map<Rule, Double> populateSupportMap() {
		Map<Rule, Double> map = new HashMap<Rule, Double>();
		for (Rule rule : ruleModel.getRules()) {
			int labelIndex = getLabelNames().indexOf(rule.getLabel());
			map.put(rule, new Double(rule.getFrequencies()[labelIndex] / getNumCasos()));
		}
		return map;
	}

	/**
	 * Devuelve una lista con los nombres de todas las predicciones posibles.
	 * 
	 * @return List<String> lista que contiene las predicciones
	 */
	public List<String> getLabelNames() {
		return ruleModel.getLabel().getMapping().getValues();
	}

	/**
	 * Devuelve la precisi�n de la regla.
	 * 
	 * @param rule
	 * @return
	 */
	public Double getRuleConfidence(Rule rule) {
		return precisiones.get(rule);
	}

	/**
	 * Devuelve el soporte de la regla
	 * 
	 * @param rule
	 * @return
	 */
	public Double getRuleSupport(Rule rule) {
		return soportes.get(rule);
	}

}