if (!world.isClientSide())
    PacketDistributor.sendToAllPlayers(new ${field$packet?replace('CUSTOM:', '')}Message(""));