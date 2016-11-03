#pragma once

#include <mbgl/gl/program.hpp>
#include <mbgl/programs/attributes.hpp>

#include <cassert>

namespace mbgl {

enum class ProgramDefines : bool {
    None = false,
    Overdraw = true,
};

template <class Shaders, class Primitive, class Attributes, class Uniforms>
class Program : public gl::Program<Primitive, Attributes, Uniforms> {
public:
    using ParentType = gl::Program<Primitive, Attributes, Uniforms>;

    Program(gl::Context& context, ProgramDefines defines)
        : ParentType(context, Shaders::vertexSource, fragmentSource(defines))
        {}

    static std::string fragmentSource(ProgramDefines defines) {
        std::string fragment = Shaders::fragmentSource;
        if (defines == ProgramDefines::Overdraw) {
            assert(fragment.find("#ifdef OVERDRAW_INSPECTOR") != std::string::npos);
            fragment.replace(fragment.find_first_of('\n'), 1, "\n#define OVERDRAW_INSPECTOR\n");
        }
        return fragment;
    }
};

template <class Shaders, class Primitive, class LayoutAttrs, class Uniforms, class PaintProperties>
class PaintProgram : public Program<Shaders, Primitive, LayoutAttrs, Uniforms> {
public:
    using ParentType = Program<Shaders, Primitive, LayoutAttrs, Uniforms>;
    using ParentType::ParentType;

    using typename ParentType::UniformValues;
    using typename ParentType::Vertex;

    using LayoutAttributes = typename ParentType::Attributes;
    using PaintAttributes = attributes::PaintAttributes<PaintProperties>;

    template <class DrawMode, class AllPaintProperties>
    void draw(gl::Context& context,
              DrawMode drawMode,
              gl::DepthMode depthMode,
              gl::StencilMode stencilMode,
              gl::ColorMode colorMode,
              UniformValues&& uniformValues,
              const gl::VertexBuffer<Vertex>& vertexBuffer,
              const gl::IndexBuffer<DrawMode>& indexBuffer,
              const std::vector<gl::Segment>& segments,
              const AllPaintProperties& properties) {
        context.draw({
            std::move(drawMode),
            std::move(depthMode),
            std::move(stencilMode),
            std::move(colorMode),
            ParentType::program,
            vertexBuffer.buffer,
            indexBuffer.buffer,
            segments,
            ParentType::Uniforms::binder(ParentType::uniformsState, std::move(uniformValues)),
            [&] (std::size_t vertexOffset) {
                LayoutAttributes::binder(ParentType::attributesState)(vertexOffset);
                PaintAttributes::binder(paintAttributesState, properties)(vertexOffset);
            }
        });
    }

private:
    typename PaintAttributes::State paintAttributesState;
};

} // namespace mbgl
