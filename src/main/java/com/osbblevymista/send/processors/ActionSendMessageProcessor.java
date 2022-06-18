package com.osbblevymista.send.processors;

import com.osbblevymista.keyabords.KeyboardParam;
import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.BasePage;
import com.osbblevymista.pages.PageParams;
import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageBuilder;
import com.osbblevymista.send.SendMessageParams;
import com.osbblevymista.system.Messages;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ActionSendMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(ActionSendMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;

    public List<BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>> createSendMessageList(OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {
        ArrayList<BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>> arrayList = new ArrayList<>();

        if (Objects.isNull(osbbKeyboardButton)) {
            arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                              @Override
                              public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                  return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
                              }
                          }
            );
        } else {
            BasePage page = getNextPage(osbbKeyboardButton);

            if (osbbKeyboardButton.messages.size() > 0) {
                arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                                  @Override
                                  public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                      try {
                                          return createButtonMessage(sendMessageParams, osbbKeyboardButton);
                                      } catch (UnsupportedEncodingException | URISyntaxException e) {
                                          e.printStackTrace();
                                          logger.error(e.getMessage(), e);
                                      }
                                      return null;
                                  }
                              }
                );
            }

            if (Objects.nonNull(page)) {

                if (page.messages.size() > 0) {
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                                      @Override
                                      public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              return createPageMessage(sendMessageParams, osbbKeyboardButton);
                                          } catch (UnsupportedEncodingException | URISyntaxException e) {
                                              e.printStackTrace();
                                              logger.error(e.getMessage(), e);
                                          }
                                          return null;
                                      }
                                  }
                    );
                }

                if (page.canExecute()) {
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                                      @Override
                                      public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              return createPageExecution(sendMessageParams, osbbKeyboardButton);
                                          } catch (UnsupportedEncodingException | URISyntaxException e) {
                                              e.printStackTrace();
                                              logger.error(e.getMessage(), e);
                                          }
                                          return null;
                                      }
                                  }
                    );
                }

                if (page.isOSBBKeyboard()) {
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                                      @Override
                                      public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              return createPageKeyboard(sendMessageParams, osbbKeyboardButton);
                                          } catch (UnsupportedEncodingException | URISyntaxException e) {
                                              e.printStackTrace();
                                              logger.error(e.getMessage(), e);
                                          }
                                          return null;
                                      }
                                  }
                    );
                }
            }

            if (osbbKeyboardButton.canExecute()) {
                arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                                  @Override
                                  public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                      try {
                                          return createButtonExecution(sendMessageParams, osbbKeyboardButton);
                                      } catch (URISyntaxException | IOException e) {
                                          e.printStackTrace();
                                          logger.error(e.getMessage(), e);
                                      }
                                      return null;
                                  }
                              }
                );
            }
        }

        if (arrayList.isEmpty()) {
            arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>() {
                              @Override
                              public OSBBSendMessage apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                  return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
                              }
                          }
            );
        }
        return arrayList;
    }

    public Function<Message, OSBBSendMessage> createSimpleMessage(SendMessageParams sendMessageParam, String text){
        return new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {
                try {
                    return sendMessageBuilder.createSimpleMessage(sendMessageParam, text);
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(),e);
                }
                return null;
            }
        };
    }

    private BasePage getNextPage(OSBBKeyboardButton osbbKeyboardButton) {
        BasePage nextPage = osbbKeyboardButton.getNextPage();
        BasePage prevPage = osbbKeyboardButton.getPrevPage();

        BasePage page = null;

        if (nextPage != null) {
            page = nextPage;
        } else if (prevPage != null) {
            page = prevPage;
        }

        return page;
    }

    private OSBBSendMessage createPageMessage(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            UnsupportedEncodingException, URISyntaxException {

        BasePage page = getNextPage(osbbKeyboardButton);

        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());
        StringBuilder messageBuffer = new StringBuilder();

        if (page.messages.size() > 0) {
            messageBuffer.delete(0, messageBuffer.length());
            messageBuffer.append(getMessage(page.messages)).append("\n");
            sendMessage.setText(messageBuffer.toString());
            return sendMessage;
        } else {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }
    }

    private OSBBSendMessage createButtonMessage(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            UnsupportedEncodingException, URISyntaxException {

        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());
        StringBuilder messageBuffer = new StringBuilder();

        if (osbbKeyboardButton.messages.size() > 0) {
            messageBuffer.delete(0, messageBuffer.length());
            messageBuffer.append(getMessage(osbbKeyboardButton.messages)).append("\n");
            sendMessage.setText(messageBuffer.toString());
            return sendMessage;
        } else {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }
    }

    private OSBBSendMessage createPageKeyboard(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            UnsupportedEncodingException, URISyntaxException {

        BasePage page = getNextPage(osbbKeyboardButton);
        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());
        StringBuilder messageBuffer = new StringBuilder();

        if (Objects.nonNull(page) && page.isOSBBKeyboard()) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(page.getOsbbKeyboard().getKeyBoard());
            replyKeyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            messageBuffer.append(page.getTitle()).append("\n");

            sendMessage.setText(messageBuffer.toString());
            return sendMessage;
        } else {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }
    }

    private OSBBSendMessage createButtonExecution(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            IOException, URISyntaxException {

        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());

        boolean initSendMessage = false;
        StringBuffer messageBuffer = new StringBuffer();

        if (osbbKeyboardButton.canExecute()) {
            KeyboardParam keyboardParam = KeyboardParam
                    .builder()
                    .login(sendMessageParams.getLogin())
                    .pass(sendMessageParams.getPass())
                    .build();

            initSendMessage = osbbKeyboardButton.doExecute(keyboardParam, o -> {
                List<List<InlineKeyboardButton>> keyboardButtons = o.getInlineKeyboardButtons();
                if (Objects.nonNull(o.messages)) {
                    messageBuffer.append(String.join("\n", o.messages)).append("\n");
                    sendMessage.setText(messageBuffer.toString());
                    return true;
                }

                if (Objects.nonNull(keyboardButtons) && keyboardButtons.size() > 0) {
                    sendMessage.setReplyMarkup(new InlineKeyboardMarkup(keyboardButtons));
                    messageBuffer.append(o.getTitle()).append("\n");
                    sendMessage.setText(messageBuffer.toString());

                    return true;
                }
                return false;
            });
        }

        if (!initSendMessage) {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }

        return sendMessage;
    }

    private OSBBSendMessage createPageExecution(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            UnsupportedEncodingException, URISyntaxException {
        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());

        BasePage page = getNextPage(osbbKeyboardButton);
        boolean initSendMessage = false;
        StringBuffer messageBuffer = new StringBuffer();

        if (Objects.nonNull(page) && page.canExecute()) {
            messageBuffer.delete(0, messageBuffer.length());
            PageParams pageParams = PageParams
                    .builder()
                    .login(sendMessageParams.getLogin())
                    .pass(sendMessageParams.getPass())
                    .build();

            initSendMessage = page.doExecute(pageParams, o -> {
                List<List<InlineKeyboardButton>> keyboardButtons = o.getInlineKeyboardButtons();

                messageBuffer.append(o.getTitle()).append("\n");

                if (!o.messages.isEmpty()) {
                    messageBuffer.append(getMessage(o.messages)).append("\n");
                }

                if (Objects.nonNull(keyboardButtons) && keyboardButtons.size() > 0) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboardButtons);

                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                }

                sendMessage.setText(messageBuffer.toString());

                return StringUtils.isNotEmpty(messageBuffer);
            });
        }

        if (!initSendMessage) {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }

        return sendMessage;
    }

    private String getMessage(List<String> messages) {
        return messages
                .stream()
                .map(s -> s + "\n")
                .collect(Collectors.joining());
    }

}
