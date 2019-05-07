package com.validationurl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

	private static final char[] CLIENT_NULL = new char[] { 123, 34, 99, 108, 105, 101, 110, 116, 34, 58, 32, 110, 117, 108, 108,
			44, 32, 34, 114, 101, 103, 101, 120, 34, 58, 32, 34, 46, 42, 103, 111, 111, 103, 108, 101, 46, 42, 34,
			125 };

	private static final char[] CLIENT = new char[] { 123, 34, 99, 108, 105, 101, 110, 116, 34, 58, 32, 34, 99, 108, 105, 101,
			110, 116, 32, 114, 97, 98, 98, 105, 116, 34, 44, 32, 34, 114, 101, 103, 101, 120, 34, 58, 32, 34, 91, 97,
			45, 122, 93, 34, 125 };

	@Test
	public void saveClientWhiteList() {
		when(this.whiteListRep.save(any(WhiteList.class))).thenReturn(null);

		this.insertUrlConsumer.receive(CLIENT);

		verify(this.whiteListRep).save(any(WhiteList.class));

	}
	@Test
	public void saveWhiteListGlobal() {
		when(this.whiteListGlobalRep.save(any(WhiteListGlobal.class))).thenReturn(null);

		this.insertUrlConsumer.receive(CLIENT_NULL);

		verify(this.whiteListGlobalRep).save(any(WhiteListGlobal.class));

	}

}
