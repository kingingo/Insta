package net.insta.base.event.events.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.insta.base.event.Event;

@AllArgsConstructor
@Getter
public class LoginEvent extends Event{
	private String email;
	private String password;
}