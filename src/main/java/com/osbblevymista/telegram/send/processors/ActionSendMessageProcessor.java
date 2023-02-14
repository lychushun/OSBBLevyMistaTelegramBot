package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.api.services.MiyDimService;
import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.keyabords.KeyboardParam;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.BasePage;
import com.osbblevymista.telegram.pages.PageParams;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.Messages;
import lombok.AllArgsConstructor;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@AllArgsConstructor
@Component
public class ActionSendMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(ActionSendMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final MiyDimService miyDimService;


    public List<BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>> createSendMessageList(OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {
        ArrayList<BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>> arrayList = new ArrayList<>();

        if (Objects.isNull(osbbKeyboardButton)) {
            arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                              @Override
                              public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                  return createHomePageMessageList(sendMessageParams);
                              }
                          }
            );
        } else {
            BasePage page = getNextPage(osbbKeyboardButton);

            if (osbbKeyboardButton.messages.size() > 0) {
                arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                                  @Override
                                  public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                      try {
                                          List<OSBBSendMessage> list = new ArrayList<>();
                                          list.add(createButtonMessage(sendMessageParams, osbbKeyboardButton));
                                          return list;
                                      } catch (UnsupportedEncodingException | URISyntaxException e) {
                                          e.printStackTrace();
                                          logger.error(e.getMessage(), e);
                                      }
                                      return null;
                                  }
                              }
                );
            }

            if (nonNull(page)) {

                if (page.getMessages().size() > 0) {
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                                      @Override
                                      public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              List<OSBBSendMessage> list = new ArrayList<>();
                                              list.add(createPageMessage(sendMessageParams, osbbKeyboardButton));
                                              return list;
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
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                                      @Override
                                      public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              List<OSBBSendMessage> list = new ArrayList<>();
                                              list.add(createPageKeyboard(sendMessageParams, osbbKeyboardButton));
                                              return list;
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
                    arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                                      @Override
                                      public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                          try {
                                              List<OSBBSendMessage> list = new ArrayList<>();
                                              list.add(createPageExecution(sendMessageParams, osbbKeyboardButton));
                                              return list;
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
                arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                                  @Override
                                  public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                      try {
                                          List<OSBBSendMessage> list = new ArrayList<>();
                                          list.add(createButtonExecution(sendMessageParams, osbbKeyboardButton));
                                          return list;
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
            arrayList.add(new BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>() {
                              @Override
                              public List<OSBBSendMessage> apply(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) {
                                  return createHomePageMessageList(sendMessageParams);
                              }
                          }
            );
        }
        return arrayList;
    }

    public Function<Message, OSBBSendMessage> createSimpleMessage(SendMessageParams sendMessageParam, String text) {
        return new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {
                try {
                    return sendMessageBuilder.createSimpleMessage(sendMessageParam, text);
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                return null;
            }
        };
    }

    public BotExecutionObject createHomePageMessage(SendMessageParams sendMessageParam) {
        BotExecutionObject botExecutionObject = new BotExecutionObject();
        botExecutionObject.setExecution(sendMessageBuilder.goHomeMessage(sendMessageParam, Messages.UNRECOGNIZED_COMMAND.getMessage()));
        return botExecutionObject;
    }

    public List<OSBBSendMessage> createHomePageMessageList(SendMessageParams sendMessageParam) {
        List<OSBBSendMessage> osbbSendMessages = new ArrayList<>();
        OSBBSendMessage osbbSendMessage = sendMessageBuilder.goHomeMessage(sendMessageParam, Messages.UNRECOGNIZED_COMMAND.getMessage());
        osbbSendMessages.add(osbbSendMessage);
        return osbbSendMessages;
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

        if (page.getMessages().size() > 0) {
            messageBuffer.delete(0, messageBuffer.length());
            messageBuffer.append(getMessage(page.getMessages())).append("\n");
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

        if (nonNull(page) && page.isOSBBKeyboard()) {
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

        AtomicReference<OSBBSendMessage> sendMessage = new AtomicReference<>(sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId()));

        boolean initSendMessage = false;

        if (osbbKeyboardButton.canExecute()) {
            KeyboardParam keyboardParam = KeyboardParam
                    .builder()
                    .clientIp(sendMessageParams.getClientIp())
                    .clientPort(sendMessageParams.getClientPort())
                    .chatId(sendMessageParams.getChatIdAsString())
                    .miyDimCookie(miyDimService.getCookie(sendMessageParams.getChatIdAsString()))
                    .build();

            initSendMessage = osbbKeyboardButton.doExecute(keyboardParam, o -> {
                sendMessage.set(createExecution(sendMessageParams, o));
                return true;
            });
        }

        if (!initSendMessage) {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }

        return sendMessage.get();
    }

    private OSBBSendMessage createPageExecution(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton) throws
            UnsupportedEncodingException, URISyntaxException {

        AtomicReference<OSBBSendMessage> sendMessage = new AtomicReference<>(sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId()));

        BasePage page = getNextPage(osbbKeyboardButton);
        boolean initSendMessage = false;
        if (nonNull(page) && page.canExecute()) {
            PageParams pageParams = PageParams
                    .builder()
                    .clientIp(sendMessageParams.getClientIp())
                    .clientPort(sendMessageParams.getClientPort())
                    .chatId(sendMessageParams.getChatId())
                    .cookie(miyDimService.getCookie(sendMessageParams.getChatIdAsString()))
                    .build();

            initSendMessage = page.doExecute(pageParams, o -> {

                sendMessage.set(createExecution(sendMessageParams, o));
                return true;
            });
        }

        if (!initSendMessage) {
            return sendMessageBuilder.createEmptyMessage(sendMessageParams.getChatId());
        }

        return sendMessage.get();
    }

    private OSBBSendMessage createExecution(SendMessageParams sendMessageParams, ExecutorListenerResponse o) {

        StringBuilder messageBuffer = new StringBuilder();
        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParams.getChatId());
        List<List<InlineKeyboardButton>> keyboardButtons = o.getInlineKeyboardButtons();

        messageBuffer.append(o.getTitle()).append("\n");

        if (!o.messages.isEmpty()) {
            messageBuffer.append(getMessage(o.messages)).append("\n");
        }

        if (nonNull(keyboardButtons) && keyboardButtons.size() > 0) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(keyboardButtons);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        sendMessage.setText(messageBuffer.toString());

        return sendMessage;
    }

    private String getMessage(List<String> messages) {
        return messages
                .stream()
                .map(s -> s + "\n")
                .collect(Collectors.joining());
    }

}
