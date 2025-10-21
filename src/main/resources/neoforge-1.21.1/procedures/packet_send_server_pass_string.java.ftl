if (world.isClientSide())
    PacketDistributor.sendToServer(new ${field$packet?replace('CUSTOM:', '')}Message(${input$string}));