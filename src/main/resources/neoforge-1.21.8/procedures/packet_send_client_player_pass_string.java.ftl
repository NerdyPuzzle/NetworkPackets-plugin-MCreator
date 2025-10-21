if (${input$entity} instanceof ServerPlayer player${cbi})
    PacketDistributor.sendToPlayer(player${cbi}, new ${field$packet?replace('CUSTOM:', '')}Message(${input$string}));