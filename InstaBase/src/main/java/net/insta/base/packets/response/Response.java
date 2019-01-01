package net.insta.base.packets.response;

import java.util.UUID;

import lombok.Getter;
import net.insta.base.client.Client;
import net.insta.base.packets.Packet;
import net.insta.base.utils.Callback;

public class Response<T extends Packet> implements ResponseListener{

	@Getter
	private UUID uuid;
	@Getter
	private Client client;
	private boolean hasResponse=false;
	private T response;
	private long timeout = 5000;
	private long start;
	private long sleep = 25;
	
	public Response(Client client,UUID uuid) {
		this.uuid=uuid;
		this.start=System.currentTimeMillis();
		this.client=client;
		addListener();
	}
	
	private void responsed(T response) {
		this.response=response;
		this.hasResponse=true;
	}
	
	public T getSync() throws NullPointerException{
		return getSync(timeout);
	}
	
	public T getSync(long timeout) throws NullPointerException{
		while(!hasResponse) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {}
			
			if( (start+timeout) < System.currentTimeMillis() ) {
				removeListener();
				throw new NullPointerException("No response -> timeout");
			}
		}
		return response;
	}
	
	public void getAsync(Callback<T> callback) {
		getAsync(callback,timeout);
	}
	
	public void getAsync(Callback<T> callback, long timeout) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				T response = getSync(timeout);
				callback.run(response);
			}
		}).start();
	}
	
	public void removeListener() {
		if(this.client!=null)this.client.getReader().getListeners().remove(uuid);
	}
	
	public void addListener() {
		if(this.client!=null)this.client.getReader().getListeners().put(uuid, this);
	}

	public boolean isInstanceOf(Class<T> clazz, Class<T> targetClass) {
	    return clazz.isInstance(targetClass);
	}
	
	@SuppressWarnings("unchecked")
	public void handle(UUID uuid, Packet packet) {
		if(uuid.equals(this.uuid))
			responsed( (T) packet );
	}
}
