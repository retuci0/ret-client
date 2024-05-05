package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;

public class ReachEvent extends Event {

	private float reach;

	public ReachEvent(float reach) {
		this.reach = reach;
	}

	public float getReach() {
		return reach;
	}

	public void setReach(float reach) {
		this.reach = reach;
	}
}