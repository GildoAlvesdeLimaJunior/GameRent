package GameRent;

public class Fidelidade{
	private Cliente cliente;
	private int pontos;
	private static int meta = 5;
	
	public  int getPontos(){
		return pontos;
	}
	public static void setMeta(int meta){
		meta=meta;
	}
	public static int getMeta(){
		return meta;
	}
	public void acumularPonto(){
		pontos++;
	}
	public boolean podeResgatarLocacaoGratis(){
		return pontos>=meta;
	}
	public void resetarPontos(){
		pontos=0;
	}
	public boolean resgatarLocacaoGratis(){
		if (podeResgatarLocacaoGratis()){
			resetarPontos();
			return true;
		}
		return false;
	}
}
