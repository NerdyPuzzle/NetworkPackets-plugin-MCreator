if (world.isClientSide())
    ClientPacketDistributor.sendToServer(new ${field$packet?replace('CUSTOM:', '')}Message(""));