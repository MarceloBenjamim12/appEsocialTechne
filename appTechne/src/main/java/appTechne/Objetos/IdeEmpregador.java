package appTechne.Objetos;

public class IdeEmpregador {
	
	private String tpInsc;
	private String nrInsc;
	
	public String getTpInsc() {
		return tpInsc;
	}
	public void setTpInsc(String tpInsc) {
		this.tpInsc = tpInsc;
	}
	public String getNrInsc() {
		return nrInsc;
	}
	public void setNrInsc(String nrInsc) {
		this.nrInsc = nrInsc;
	}
	
	@Override
	public String toString() {
		return "ideEmpregador [tpInsc=" + tpInsc + ", nrInsc=" + nrInsc + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nrInsc == null) ? 0 : nrInsc.hashCode());
		result = prime * result + ((tpInsc == null) ? 0 : tpInsc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdeEmpregador other = (IdeEmpregador) obj;
		if (nrInsc == null) {
			if (other.nrInsc != null)
				return false;
		} else if (!nrInsc.equals(other.nrInsc))
			return false;
		if (tpInsc == null) {
			if (other.tpInsc != null)
				return false;
		} else if (!tpInsc.equals(other.tpInsc))
			return false;
		return true;
	}
}
