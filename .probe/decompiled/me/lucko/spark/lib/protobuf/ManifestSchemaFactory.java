package me.lucko.spark.lib.protobuf;

@CheckReturnValue
final class ManifestSchemaFactory implements SchemaFactory {

    private final MessageInfoFactory messageInfoFactory;

    private static final MessageInfoFactory EMPTY_FACTORY = new MessageInfoFactory() {

        @Override
        public boolean isSupported(Class<?> clazz) {
            return false;
        }

        @Override
        public MessageInfo messageInfoFor(Class<?> clazz) {
            throw new IllegalStateException("This should never be called.");
        }
    };

    public ManifestSchemaFactory() {
        this(getDefaultMessageInfoFactory());
    }

    private ManifestSchemaFactory(MessageInfoFactory messageInfoFactory) {
        this.messageInfoFactory = Internal.checkNotNull(messageInfoFactory, "messageInfoFactory");
    }

    @Override
    public <T> Schema<T> createSchema(Class<T> messageType) {
        SchemaUtil.requireGeneratedMessage(messageType);
        MessageInfo messageInfo = this.messageInfoFactory.messageInfoFor(messageType);
        if (messageInfo.isMessageSetWireFormat()) {
            return GeneratedMessageLite.class.isAssignableFrom(messageType) ? MessageSetSchema.newSchema(SchemaUtil.unknownFieldSetLiteSchema(), ExtensionSchemas.lite(), messageInfo.getDefaultInstance()) : MessageSetSchema.newSchema(SchemaUtil.proto2UnknownFieldSetSchema(), ExtensionSchemas.full(), messageInfo.getDefaultInstance());
        } else {
            return newSchema(messageType, messageInfo);
        }
    }

    private static <T> Schema<T> newSchema(Class<T> messageType, MessageInfo messageInfo) {
        if (GeneratedMessageLite.class.isAssignableFrom(messageType)) {
            return isProto2(messageInfo) ? MessageSchema.newSchema(messageType, messageInfo, NewInstanceSchemas.lite(), ListFieldSchema.lite(), SchemaUtil.unknownFieldSetLiteSchema(), ExtensionSchemas.lite(), MapFieldSchemas.lite()) : MessageSchema.newSchema(messageType, messageInfo, NewInstanceSchemas.lite(), ListFieldSchema.lite(), SchemaUtil.unknownFieldSetLiteSchema(), null, MapFieldSchemas.lite());
        } else {
            return isProto2(messageInfo) ? MessageSchema.newSchema(messageType, messageInfo, NewInstanceSchemas.full(), ListFieldSchema.full(), SchemaUtil.proto2UnknownFieldSetSchema(), ExtensionSchemas.full(), MapFieldSchemas.full()) : MessageSchema.newSchema(messageType, messageInfo, NewInstanceSchemas.full(), ListFieldSchema.full(), SchemaUtil.proto3UnknownFieldSetSchema(), null, MapFieldSchemas.full());
        }
    }

    private static boolean isProto2(MessageInfo messageInfo) {
        return messageInfo.getSyntax() == ProtoSyntax.PROTO2;
    }

    private static MessageInfoFactory getDefaultMessageInfoFactory() {
        return new ManifestSchemaFactory.CompositeMessageInfoFactory(GeneratedMessageInfoFactory.getInstance(), getDescriptorMessageInfoFactory());
    }

    private static MessageInfoFactory getDescriptorMessageInfoFactory() {
        try {
            Class<?> clazz = Class.forName("me.lucko.spark.lib.protobuf.DescriptorMessageInfoFactory");
            return (MessageInfoFactory) clazz.getDeclaredMethod("getInstance").invoke(null);
        } catch (Exception var1) {
            return EMPTY_FACTORY;
        }
    }

    private static class CompositeMessageInfoFactory implements MessageInfoFactory {

        private MessageInfoFactory[] factories;

        CompositeMessageInfoFactory(MessageInfoFactory... factories) {
            this.factories = factories;
        }

        @Override
        public boolean isSupported(Class<?> clazz) {
            for (MessageInfoFactory factory : this.factories) {
                if (factory.isSupported(clazz)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public MessageInfo messageInfoFor(Class<?> clazz) {
            for (MessageInfoFactory factory : this.factories) {
                if (factory.isSupported(clazz)) {
                    return factory.messageInfoFor(clazz);
                }
            }
            throw new UnsupportedOperationException("No factory is available for message type: " + clazz.getName());
        }
    }
}