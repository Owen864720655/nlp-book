package templateSeg;

public class POSType {
	public String pos; //词
	public int freq; //词频

	public POSType(String w, int f) {
		pos = w;
		freq = f;
	}

	public String toString() {
		return pos + ":" + freq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + freq;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
		POSType other = (POSType) obj;
		if (freq != other.freq)
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
}
