package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

<<<<<<< HEAD
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Before;
=======
>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.validationurl.consumer.InsertUrlConsumer;
import com.validationurl.model.WhiteList;
import com.validationurl.model.WhiteListGlobal;
import com.validationurl.repository.WhiteListGlobalRepository;
import com.validationurl.repository.WhiteListRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { InsertUrlConsumer.class })
public class InsertUrlTest {

	@MockBean
	private WhiteListGlobalRepository whiteListGlobalRep;

	@MockBean
	private WhiteListRepository whiteListRep;

	@Autowired
	@InjectMocks
	private InsertUrlConsumer insertUrlConsumer;

<<<<<<< HEAD
	@Before
	public void init() {
	}
	
	//@Test
	public void requestAscii() throws UnsupportedEncodingException {
		char[] payloadChars = new char[] { 123,34,99,108,105,101,110,116,34,58,32,110,117,108,108,44,32,34,114,101,103,101,120,34,58,32,34,46,42,103,111,111,103,108,101,46,42,34,125 };
		
=======
	@Test
	public void saveClientWhiteList() {
		String payload = "{\"client\": \"client rabbit\", \"regex\": \"[a-z]\"}";

>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git
		when(this.whiteListRep.save(any(WhiteList.class))).thenReturn(null);

		this.insertUrlConsumer.receive(payloadChars);

		verify(this.whiteListRep).save(any(WhiteList.class));
		
	}

	//@Test
	public void saveClientWhiteList() throws IOException {
		String payload = "{\"client\": \"client rabbit\", \"regex\": \"[a-z]\"}";
		
		byte[] ascii = payload.getBytes(StandardCharsets.US_ASCII); 
		String asciiString = Arrays.toString(ascii).replace("[", "").replace("]", "");
		System.out.println(asciiString);


	}

<<<<<<< HEAD
	//@Test
	public void saveWhiteListGlobal() throws IOException {
=======
	@Test
	public void saveWhiteListGlobal() {
>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git
		String payload = "{\"client\": null, \"regex\": \"[a-z]\"}";

		when(this.whiteListGlobalRep.save(any(WhiteListGlobal.class))).thenReturn(null);

	//this.insertUrlConsumer.receive(payload);

		verify(this.whiteListGlobalRep).save(any(WhiteListGlobal.class));

	}

<<<<<<< HEAD
	//@Test
	public void invalidJSONIsReceived() throws IOException {
=======
	@Test
	public void invalidJSONIsReceived() {
>>>>>>> branch 'master' of https://github.com/lermen-andrefabiano/validation-url.git
		String payload = "{\"invalid\": \"client rabbit\", \"regex\": \"[a-z]\"}";

		//this.insertUrlConsumer.receive(payload);

		verify(this.whiteListRep, never()).save(any(WhiteList.class));

	}

}
