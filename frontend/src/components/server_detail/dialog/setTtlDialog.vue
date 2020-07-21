<template>
  <v-row justify="center">
    <v-dialog v-model="visible" persistent max-width="600px">
      <v-card>
        <v-form
          ref="setTtlForm"
          v-model="valid"
        >
          <v-card-title>
            Set TTL
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field type="number"
                                :rules="[rules.required, rules.number]"
                                v-model="localTtl"
                                label="TTL"></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="setTtl">Change</v-btn>
            <v-btn color="blue darken-1" text @click="closeDialog">Close</v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: 'setTtlDialog',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    ttl: {
      type: Number
    }
  },
  data () {
    return {
      localTtl: null,
      valid: true,
      rules: {
        required: value => !!value || 'Required.',
        number: value => {
          return parseInt(value) === parseFloat(value) || 'Only Integer.'
        }
      }
    }
  },
  watch: {
    ttl (val) {
      this.localTtl = parseInt(val)
    },
    visible () {
      if (this.$refs.setTtlForm) {
        this.$refs.setTtlForm.resetValidation()
      }
    }
  },
  methods: {
    setTtl () {
      if (this.$refs.setTtlForm.validate()) {
        this.$emit('set-ttl', this.localTtl)
      }
    },
    closeDialog () {
      this.$emit('close-dialog')
    }
  }
}
</script>

<style scoped>

</style>
