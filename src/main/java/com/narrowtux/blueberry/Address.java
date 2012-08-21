package com.narrowtux.blueberry;

import java.net.InetAddress;

public class Address {
	private InetAddress address;
	private int port;
	
	public Address(InetAddress address, int port) {
		super();
		this.address = address;
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public InetAddress getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + port;
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
		Address other = (Address) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
