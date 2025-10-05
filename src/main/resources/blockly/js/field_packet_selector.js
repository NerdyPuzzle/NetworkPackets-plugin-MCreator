class FieldPacketSelector extends Blockly.Field {

    EDITABLE = true;
    SERIALIZABLE = true;
    CURSOR = 'default';

    constructor(side = 'server', opt_validator, opt_config) {
        super('', opt_validator, opt_config);

        this.side = side;
        this.maxDisplayLength = 75;
        this.lastClickTime = -1;

        if (opt_config)
            this.configure_(opt_config);

        let thisField = this;
        this.setTooltip(function () {
            return thisField.getValue() ?
                thisField.readableName :
                javabridge.t('blockly.field_data_list_selector.tooltip.empty');
        });
    }

    static getDefaultText() {
        return javabridge.t('blockly.extension.data_list_selector.no_entry');
    }

    configure_(config) {
        super.configure_(config);

        let opt_side = Blockly.utils.parsing.replaceMessageReferences(config['side']);
        if (opt_side) {
            this.side = opt_side;
        }
    }

    static fromJson(options) {
        const side = Blockly.utils.parsing.replaceMessageReferences(options['side']);
        return new this(side, undefined, options);
    }

    onMouseDown_(e) {
        if (this.sourceBlock_ && !this.sourceBlock_.isInFlyout) {
            if (this.lastClickTime !== -1 && ((new Date().getTime() - this.lastClickTime) < 500)) {
                e.stopPropagation();
                let thisField = this;

                packetbridge.openDataListEntrySelector(this.side, {
                    'callback': function (value, readableName) {
                        thisField.cachedReadableName = readableName || value;
                        const group = Blockly.Events.getGroup();
                        Blockly.Events.setGroup(true);
                        thisField.setValue(value);
                        Blockly.Events.setGroup(group);
                        javabridge.triggerEvent();
                    }
                });
            } else {
                this.lastClickTime = new Date().getTime();
            }
        }
    }

    doValueUpdate_(newValue) {
        if (newValue !== this.value_) {
            this.updateReadableName(newValue);
        }
        super.doValueUpdate_(newValue);
    }

    getText_() {
        return this.readableName || FieldPacketSelector.getDefaultText();
    }

    updateReadableName(value) {
        if (this.cachedReadableName) {
            this.readableName = this.cachedReadableName;
            this.cachedReadableName = null;
        }
        else if (value) {
            this.readableName = javabridge.getReadableNameOf(value, 'packet') || value;
        } else {
            this.readableName = FieldPacketSelector.getDefaultText();
        }
    }
}

Blockly.fieldRegistry.register('field_packet_selector', FieldPacketSelector);