package ru.itpark.encoderdecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itpark.dto.chat.message.MessageDto;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDtoEncoderDecoder
    implements Encoder.Text<MessageDto>, Decoder.Text<MessageDto> {
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public MessageDto decode(String s) throws DecodeException {
    try {
      return mapper.readValue(s, MessageDto.class);
    } catch (IOException e) {
      throw new DecodeException(s, "Can't decode", e);
    }
  }

  @Override
  public boolean willDecode(String s) {
    return true;
  }

  @Override
  public String encode(MessageDto object) throws EncodeException {
    try {
      return mapper.writeValueAsString(object);
    } catch (IOException e) {
      throw new EncodeException(object, "Can't encode", e);
    }
  }

  @Override
  public void init(EndpointConfig endpointConfig) {

  }

  @Override
  public void destroy() {

  }
}
