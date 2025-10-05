<#-- @formatter:off -->
<#include "procedures.java.ftl">

package ${package}.network;

import ${package}.${JavaModName};

@EventBusSubscriber public record ${name}Message(String extradata) implements CustomPacketPayload {

	public static final Type<${name}Message> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(${JavaModName}.MODID, "${registryname}"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ${name}Message> STREAM_CODEC = StreamCodec.of(
			(RegistryFriendlyByteBuf buffer, ${name}Message message) -> {
				buffer.writeUtf(message.extradata);
			},
			(RegistryFriendlyByteBuf buffer) -> new ${name}Message(buffer.readUtf())
	);

	@Override public Type<${name}Message> type() {
		return TYPE;
	}

	public static void handleData(final ${name}Message message, final IPayloadContext context) {
	    <#if hasProcedure(data.serverBound)>
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
			    Player entity = context.player();
			    Level world = entity.level();
			    double x = entity.getX();
			    double y = entity.getY();
			    double z = entity.getZ();
			    String inboundString = message.extradata;

	            if (!world.hasChunkAt(entity.blockPosition()))
                    return;

                <@procedureOBJToCode data.serverBound/>
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
		</#if>
	    <#if hasProcedure(data.clientBound)>
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
			    Player entity = context.player();
			    Level world = entity.level();
			    double x = entity.getX();
			    double y = entity.getY();
			    double z = entity.getZ();
			    String inboundString = message.extradata;

			    <@procedureOBJToCode data.clientBound/>
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
		</#if>
	}

	@SubscribeEvent public static void registerMessage(FMLCommonSetupEvent event) {
		${JavaModName}.addNetworkMessage(${name}Message.TYPE, ${name}Message.STREAM_CODEC, ${name}Message::handleData);
	}

}
<#-- @formatter:on -->