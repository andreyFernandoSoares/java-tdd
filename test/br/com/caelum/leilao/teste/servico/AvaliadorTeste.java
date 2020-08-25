package br.com.caelum.leilao.teste.servico;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;
import br.com.caelum.leilao.teste.builder.CriadorDeLeilao;

public class AvaliadorTeste {
	
	private Avaliador leiloeiro;
	private Usuario maria;
	private Usuario jose;
	private Usuario joao;
	
	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("Jõao");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemLances() {
		Leilao leilao = new CriadorDeLeilao().para("PS3 Novo").constroi();
		leiloeiro.avalia(leilao);
	}
	
	@Test
	public void deveEntenderLancesEmOrderCrescente() {
		Leilao leilao = new CriadorDeLeilao().para("PS3 Novo")
				.lance(joao, 250.0)
				.lance(jose, 300.0)
				.lance(maria, 450.0)
				.constroi();
		
		leiloeiro.avalia(leilao);
		
		assertEquals(450.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(250.0, leiloeiro.getMenorLance(), 0.00001);
	}
	
	@Test
	public void deveEntenderLeilaoSoComUmLance() {
		Leilao leilao = new CriadorDeLeilao().para("PS3 Novo")
				.lance(joao, 1000.0)
				.constroi();
		
		leiloeiro.avalia(leilao);
		
		assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);
	}
	
	@Test
	public void deveEncontrarOsTresMaiores() {
		Leilao leilao = new CriadorDeLeilao().para("PS3 Novo")
				.lance(joao, 100.0)
				.lance(maria, 200.0)
				.lance(joao, 300.0)
				.lance(maria, 400.0)
				.constroi();
				
		leiloeiro.avalia(leilao);
		
		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());
		assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
		assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
		assertEquals(200.0, maiores.get(2).getValor(), 0.00001);
	}
}
