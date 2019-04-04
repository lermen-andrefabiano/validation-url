package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { InsertUrlConsumer.class })
public class InsertUrlTest {

	@MockBean
	private WhiteListGlobalRepository whiteListGlobalRep;

	@MockBean
	private WhiteListRepository whiteListRep;

	@Autowired
	@InjectMocks
	private InsertUrlConsumer insertUrlConsumer;

	@Before
	public void init() {
	}

	@Test
	public void saveClientWhiteList() throws IOException {
		String payload = "{\"client\": \"client rabbit\", \"regex\": \"[a-z]\"}";

		when(this.whiteListRep.save(any(WhiteList.class))).thenReturn(null);

		this.insertUrlConsumer.receive(payload);

		verify(this.whiteListRep).save(any(WhiteList.class));

	}

	@Test
	public void saveWhiteListGlobal() throws IOException {
		String payload = "{\"client\": null, \"regex\": \"[a-z]\"}";

		when(this.whiteListGlobalRep.save(any(WhiteListGlobal.class))).thenReturn(null);

		this.insertUrlConsumer.receive(payload);

		verify(this.whiteListGlobalRep).save(any(WhiteListGlobal.class));

	}

	@Test
	public void invalidJSONIsReceived() throws IOException {
		String payload = "{\"invalid\": \"client rabbit\", \"regex\": \"[a-z]\"}";

		this.insertUrlConsumer.receive(payload);

		verify(this.whiteListRep, never()).save(any(WhiteList.class));

	}

}
